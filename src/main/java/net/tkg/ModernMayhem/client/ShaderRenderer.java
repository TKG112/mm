package net.tkg.ModernMayhem.client;


import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.mixinaccessor.PostChainAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ShaderRenderer is responsible for managing and rendering a specific shader in Minecraft.
 * It initializes the shader using a PostChain and provides methods to activate, deactivate, and render the shader.
 * This class is designed to be used in the client-side rendering pipeline, specifically for custom shaders.
 * * Note: Ensure that the shader file exists in the specified path and is correctly formatted for Minecraft's shader system.
 * @author CookieG
 */
public class ShaderRenderer {
    private final ResourceLocation shaderLocation;
    private PostChain postChain = null; // The PostChain instance that manages the shader
    private boolean isActive = false; // Indicates whether the shader is currently active
    private int lastFrameScreenWidth = -1; // Stores the last frame's screen dimensions to detect changes
    private int lastFrameScreenHeight = -1; // Stores the last frame's screen dimensions to detect changes
    private final Map<String, Pair<String, Object>> modifiedUniforms = new HashMap<>(); // Stores modified uniforms for reapplication

    private static final Minecraft mc = Minecraft.getInstance();

    /**
     * Constructs a ShaderRenderer for a specific shader.
     * @param shaderLocation - The ResourceLocation of the shader to be rendered.
     */
    public ShaderRenderer(@NotNull ResourceLocation shaderLocation) {
        this.shaderLocation = shaderLocation;
    }

    /**
     * Gets the ResourceLocation of the shader.
     * @return The ResourceLocation of the shader.
     */
    public ResourceLocation getShaderLocation() { return shaderLocation; }

    /**
     * Checks if the shader is currently active.
     * @return true if the shader is active, false otherwise.
     */
    public boolean isActive() { return isActive; }

    /**
     * Gets the name of the shader without the file extension.
     * @return The name of the shader.
     */
    public String getShaderName() {
        return shaderLocation.getNamespace() + ResourceLocation.NAMESPACE_SEPARATOR + shaderLocation.getPath().substring(shaderLocation.getPath().lastIndexOf('/') + 1, shaderLocation.getPath().lastIndexOf('.'));
    }

    public String getFullShaderName() {
        // Could have used shaderLocation.toString() but they somehow don't use their own global value NAMESPACE_SEPARATOR
        return shaderLocation.getNamespace() + ResourceLocation.NAMESPACE_SEPARATOR + shaderLocation.getPath();
    }

    /**
     * Activate the shader, allowing it to be rendered.
     * Note: You still need to call render() in your rendering loop to see the shader effect.
     */
    public void activate() { isActive = true; }

    /**
     * Deactivate the shader, preventing it from being rendered.
     * This is useful for performance optimization when the shader is not needed.
     */
    public void deactivate() { isActive = false; }

    /**
     * Toggles the active state of the shader.
     * If the shader is currently active, it will be deactivated, and vice versa.
     * Note: You still need to call render() in your rendering loop to see the shader effect.
     */
    public void toggleActive() { isActive = !isActive; }

    /**
     * Renders the shader effect.
     * This method should be called in the rendering loop, typically in the render method of your mod's client-side event handler.
     */
    public void render() {
        GameRenderer gameRenderer = mc.gameRenderer;

        if (!isActive && isCurrentEffect()) {
            gameRenderer.shutdownEffect();
            return;
        }

        if (!isActive || isCurrentEffect()) {
            return;
        }

        if (gameRenderer.currentEffect() != null && isCurrentEffect() && !hasWindowSizeChanged()) {
            return;
        }

        gameRenderer.loadEffect(shaderLocation);
        if (hasWindowSizeChanged()) reapplyModifiedUniforms();
    }

    /**
     * Gets a specific uniform from a specific shader pass.
     * @param passName - The name of the pass to search (e.g., "mm:autogain", "mm:night-vision"). If null, searches all passes.
     * @param uniformName - The name of the uniform to retrieve.
     * @return The Uniform object if found, or null if not found.
     */
    public Uniform getUniform(@Nullable String passName, String uniformName) {
        if (!isCurrentEffect() || mc.gameRenderer.currentEffect() == null) {
            return null;
        }
        List<PostPass> passes = ((PostChainAccess) Objects.requireNonNull(mc.gameRenderer.currentEffect())).test_master$getPasses();

        for (PostPass pass : passes) {
            if (passName != null && !pass.getName().equals(passName)) {
                continue;
            }

            Uniform uniform = pass.getEffect().getUniform(uniformName);
            if (uniform != null) {
                return uniform;
            }
        }
        return null;
    }

    /**
     * Gets a specific uniform from any shader pass.
     * Searches through all passes to find the uniform.
     * @param uniformName - The name of the uniform to retrieve.
     * @return The Uniform object if found, or null if not found.
     */
    public Uniform getUniform(String uniformName) {
        return getUniform(null, uniformName);
    }

    /**
     * Sets a sampler 2D uniform (texture) in the shader.
     * This method retrieves the shader pass and binds the provided texture resource to the specified sampler name.
     * @param samplerName - The name of the sampler in the shader JSON (e.g., "MaskSampler").
     * @param textureLocation - The ResourceLocation of the texture to bind.
     */
    public void setSampler2dUniform(String samplerName, ResourceLocation textureLocation) {
        // Safety check: Ensure the shader is currently loaded and active
        if (!isCurrentEffect() || mc.gameRenderer.currentEffect() == null) {
            return;
        }

        // 1. Get the texture ID from the TextureManager
        AbstractTexture texture = mc.getTextureManager().getTexture(textureLocation);
        int textureId = texture.getId();

        // 2. Access the list of passes via the Mixin accessor
        List<PostPass> passes = ((PostChainAccess) Objects.requireNonNull(mc.gameRenderer.currentEffect())).test_master$getPasses();

        // 3. Find the matching pass and bind the sampler
        for (PostPass pass : passes) {
            if (pass.getName().equals(getShaderName())) {
                // EffectInstance.setSampler takes the name and an IntSupplier for the texture ID
                pass.getEffect().setSampler(samplerName, () -> textureId);
            }
        }
    }

    /**
     * Sets a float uniform in a specific shader pass.
     * @param passName - The name of the pass (e.g., "mm:autogain", "mm:night-vision")
     * @param uniformName - The name of the uniform to set
     * @param value - The float value to set
     */
    public void setFloatUniform(String passName, String uniformName, float value) {
        Uniform uniform = getUniform(passName, uniformName);
        if (uniform != null) {
            uniform.set(value);
            modifiedUniforms.put("float:" + passName + ":" + uniformName, new Pair<>(passName + ":" + uniformName, value));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in pass {}", uniformName, passName);
        }
    }

    /**
     * Sets a float uniform in any shader pass (searches all passes).
     * @param uniformName - The name of the uniform to set
     * @param value - The float value to set
     */
    public void setFloatUniform(String uniformName, float value) {
        Uniform uniform = getUniform(null, uniformName);
        if (uniform != null) {
            uniform.set(value);
            modifiedUniforms.put("float:" + uniformName, new Pair<>(uniformName, value));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", uniformName, getShaderName());
        }
    }

    /**
     * Sets an integer uniform in a specific shader pass.
     * @param passName - The name of the pass (e.g., "mm:autogain", "mm:night-vision")
     * @param uniformName - The name of the uniform to set
     * @param value - The int value to set
     */
    public void setIntUniform(String passName, String uniformName, int value) {
        Uniform uniform = getUniform(passName, uniformName);
        if (uniform != null) {
            uniform.set(value);
            modifiedUniforms.put("int:" + passName + ":" + uniformName, new Pair<>(passName + ":" + uniformName, value));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in pass {}", uniformName, passName);
        }
    }

    /**
     * Sets an integer uniform in any shader pass (searches all passes).
     * @param uniformName - The name of the uniform to set
     * @param value - The int value to set
     */
    public void setIntUniform(String uniformName, int value) {
        Uniform uniform = getUniform(null, uniformName);
        if (uniform != null) {
            uniform.set(value);
            modifiedUniforms.put("int:" + uniformName, new Pair<>(uniformName, value));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", uniformName, getShaderName());
        }
    }

    /**
     * Sets a boolean uniform in a specific shader pass.
     * @param passName - The name of the pass (e.g., "mm:autogain", "mm:night-vision")
     * @param uniformName - The name of the uniform to set
     * @param value - The boolean value to set
     */
    public void setBooleanUniform(String passName, String uniformName, boolean value) {
        Uniform uniform = getUniform(passName, uniformName);
        if (uniform != null) {
            uniform.set(value ? 1 : 0);
            modifiedUniforms.put("bool:" + passName + ":" + uniformName, new Pair<>(passName + ":" + uniformName, value ? 1 : 0));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in pass {}", uniformName, passName);
        }
    }

    /**
     * Sets a boolean uniform in any shader pass (searches all passes).
     * @param uniformName - The name of the uniform to set
     * @param value - The boolean value to set
     */
    public void setBooleanUniform(String uniformName, boolean value) {
        Uniform uniform = getUniform(null, uniformName);
        if (uniform != null) {
            uniform.set(value ? 1 : 0);
            modifiedUniforms.put("bool:" + uniformName, new Pair<>(uniformName, value ? 1 : 0));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", uniformName, getShaderName());
        }
    }

    /**
     * Sets a vec2 uniform in a specific shader pass.
     * @param passName - The name of the pass (e.g., "mm:autogain", "mm:night-vision")
     * @param uniformName - The name of the uniform to set
     * @param x - The x component of the vec2
     * @param y - The y component of the vec2
     */
    public void setVec2Uniform(String passName, String uniformName, float x, float y) {
        Uniform uniform = getUniform(passName, uniformName);
        if (uniform != null) {
            uniform.set(x, y);
            modifiedUniforms.put("vec2:" + passName + ":" + uniformName, new Pair<>(passName + ":" + uniformName, new float[]{x, y}));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in pass {}", uniformName, passName);
        }
    }

    /**
     * Sets a vec2 uniform in any shader pass (searches all passes).
     * @param uniformName - The name of the uniform to set
     * @param x - The x component of the vec2
     * @param y - The y component of the vec2
     */
    public void setVec2Uniform(String uniformName, float x, float y) {
        Uniform uniform = getUniform(null, uniformName);
        if (uniform != null) {
            uniform.set(x, y);
            modifiedUniforms.put("vec2:" + uniformName, new Pair<>(uniformName, new float[]{x, y}));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", uniformName, getShaderName());
        }
    }

    /**
     * Sets a vec3 uniform in a specific shader pass.
     * @param passName - The name of the pass (e.g., "mm:autogain", "mm:night-vision")
     * @param uniformName - The name of the uniform to set
     * @param x - The x component of the vec3
     * @param y - The y component of the vec3
     * @param z - The z component of the vec3
     */
    public void setVec3Uniform(String passName, String uniformName, float x, float y, float z) {
        Uniform uniform = getUniform(passName, uniformName);
        if (uniform != null) {
            uniform.set(x, y, z);
            modifiedUniforms.put("vec3:" + passName + ":" + uniformName, new Pair<>(passName + ":" + uniformName, new float[]{x, y, z}));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in pass {}", uniformName, passName);
        }
    }

    /**
     * Sets a vec3 uniform in any shader pass (searches all passes).
     * @param uniformName - The name of the uniform to set
     * @param x - The x component of the vec3
     * @param y - The y component of the vec3
     * @param z - The z component of the vec3
     */
    public void setVec3Uniform(String uniformName, float x, float y, float z) {
        Uniform uniform = getUniform(null, uniformName);
        if (uniform != null) {
            uniform.set(x, y, z);
            modifiedUniforms.put("vec3:" + uniformName, new Pair<>(uniformName, new float[]{x, y, z}));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", uniformName, getShaderName());
        }
    }

    /**
     * Sets a vec4 uniform in a specific shader pass.
     * @param passName - The name of the pass (e.g., "mm:autogain", "mm:night-vision")
     * @param uniformName - The name of the uniform to set
     * @param x - The x component of the vec4
     * @param y - The y component of the vec4
     * @param z - The z component of the vec4
     * @param w - The w component of the vec4
     */
    public void setVec4Uniform(String passName, String uniformName, float x, float y, float z, float w) {
        Uniform uniform = getUniform(passName, uniformName);
        if (uniform != null) {
            uniform.set(x, y, z, w);
            modifiedUniforms.put("vec4:" + passName + ":" + uniformName, new Pair<>(passName + ":" + uniformName, new float[]{x, y, z, w}));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in pass {}", uniformName, passName);
        }
    }

    /**
     * Sets a vec4 uniform in any shader pass (searches all passes).
     * @param uniformName - The name of the uniform to set
     * @param x - The x component of the vec4
     * @param y - The y component of the vec4
     * @param z - The z component of the vec4
     * @param w - The w component of the vec4
     */
    public void setVec4Uniform(String uniformName, float x, float y, float z, float w) {
        Uniform uniform = getUniform(null, uniformName);
        if (uniform != null) {
            uniform.set(x, y, z, w);
            modifiedUniforms.put("vec4:" + uniformName, new Pair<>(uniformName, new float[]{x, y, z, w}));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", uniformName, getShaderName());
        }
    }

    /**
     * Returns a string representation of the ShaderRenderer.
     * This includes the shader location and whether the shader is active.
     * @return A string representation of the ShaderRenderer.
     */
    @Override
    public String toString() {
        return "ShaderRenderer{" +
                "shaderLocation=" + shaderLocation +
                ", isActive=" + isActive +
                '}';
    }

    /**
     * Checks if this ShaderRenderer is equal to another object.
     * Two ShaderRenderers are considered equal if they have the same shader location.
     * @param o - The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShaderRenderer that)) return false;
        return shaderLocation.equals(that.shaderLocation);
    }

    /**
     * Reapplies modified uniforms to the shader.
     * This method iterates through the modifiedUniforms map and sets each uniform in the shader.
     */
    private void reapplyModifiedUniforms() {
        // Reapply modified uniforms to the new PostChain (because uniforms are not persistent across PostChain instances)
        for (Map.Entry<String, Pair<String, Object>> entry : modifiedUniforms.entrySet()) {
            String[] keyParts = entry.getKey().split(":", 3);
            String type = keyParts[0];
            String passAndUniform = entry.getValue().getA();

            // Check if this is a pass-specific uniform
            String passName = null;
            String uniformName;
            if (passAndUniform.contains(":")) {
                String[] parts = passAndUniform.split(":", 2);
                passName = parts[0];
                uniformName = parts[1];
            } else {
                uniformName = passAndUniform;
            }

            switch (type) {
                case "float" -> {
                    float value = (float) entry.getValue().getB();
                    if (passName != null) {
                        this.setFloatUniform(passName, uniformName, value);
                    } else {
                        this.setFloatUniform(uniformName, value);
                    }
                }
                case "int" -> {
                    int value = (int) entry.getValue().getB();
                    if (passName != null) {
                        this.setIntUniform(passName, uniformName, value);
                    } else {
                        this.setIntUniform(uniformName, value);
                    }
                }
                case "bool" -> {
                    boolean value = (int) entry.getValue().getB() == 1;
                    if (passName != null) {
                        this.setBooleanUniform(passName, uniformName, value);
                    } else {
                        this.setBooleanUniform(uniformName, value);
                    }
                }
                case "vec2" -> {
                    float[] vec2Values = (float[]) entry.getValue().getB();
                    if (passName != null) {
                        this.setVec2Uniform(passName, uniformName, vec2Values[0], vec2Values[1]);
                    } else {
                        this.setVec2Uniform(uniformName, vec2Values[0], vec2Values[1]);
                    }
                }
                case "vec3" -> {
                    float[] vec3Values = (float[]) entry.getValue().getB();
                    if (passName != null) {
                        this.setVec3Uniform(passName, uniformName, vec3Values[0], vec3Values[1], vec3Values[2]);
                    } else {
                        this.setVec3Uniform(uniformName, vec3Values[0], vec3Values[1], vec3Values[2]);
                    }
                }
                case "vec4" -> {
                    float[] vec4Values = (float[]) entry.getValue().getB();
                    if (passName != null) {
                        this.setVec4Uniform(passName, uniformName, vec4Values[0], vec4Values[1], vec4Values[2], vec4Values[3]);
                    } else {
                        this.setVec4Uniform(uniformName, vec4Values[0], vec4Values[1], vec4Values[2], vec4Values[3]);
                    }
                }
                default -> ModernMayhemMod.LOGGER.warn("Unknown uniform type: {}", type);
            }
        }
    }

    /**
     * Resets the ShaderRenderer to its initial state.
     * This method deactivates the shader,
     * and clears the modified uniforms.
     */
    public void reset() {
        isActive = false;
        lastFrameScreenWidth = -1;
        lastFrameScreenHeight = -1;
        modifiedUniforms.clear();
    }

    private boolean hasWindowSizeChanged() {
        int currentScreenWidth = mc.getWindow().getGuiScaledWidth();
        int currentScreenHeight = mc.getWindow().getGuiScaledHeight();
        return lastFrameScreenWidth != currentScreenWidth || lastFrameScreenHeight != currentScreenHeight;
    }

    private boolean isCurrentEffect() {
        GameRenderer gameRenderer = mc.gameRenderer;
        PostChain currentEffect = gameRenderer.currentEffect();
        return currentEffect != null && Objects.equals(currentEffect.getName(), getFullShaderName());
    }
}
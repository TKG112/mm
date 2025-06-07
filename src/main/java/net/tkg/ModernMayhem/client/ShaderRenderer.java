package net.tkg.ModernMayhem.client;


import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.mixinaccessor.PostChainAccess;
import org.jetbrains.annotations.NotNull;
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

        // If the shader is not active and is currently loaded, we need to unload it
        if (!isActive && isCurrentEffect()) {
            gameRenderer.shutdownEffect();
            return;
        }

        // If the shader is not active or is already the current effect, we do not need to load it again
        if (!isActive || isCurrentEffect()) {
            return;
        }

        // If the shader is already loaded and the window size has not changed, we do not need to reload it
        if (gameRenderer.currentEffect() != null && isCurrentEffect() && !hasWindowSizeChanged()) {
            return;
        }

        gameRenderer.loadEffect(shaderLocation);

        // Reapply modified uniforms if the window size has changed
        if (hasWindowSizeChanged()) reapplyModifiedUniforms();
    }

    /**
     * Gets a specific uniform from the shader.
     * This method searches through the PostChain's passes to find the uniform by name.
     * @param name - The name of the uniform to retrieve.
     * @return The Uniform object if found, or null if not found or if postChain is null.
     */
    public Uniform getUniform(String name) {
        if (!isCurrentEffect() || mc.gameRenderer.currentEffect() == null) {
            return null;
        }
        List<PostPass> passes = ((PostChainAccess) Objects.requireNonNull(mc.gameRenderer.currentEffect())).test_master$getPasses();
        for (PostPass pass : passes) {
            if (pass.getName().equals(getShaderName())) {
                Uniform uniform = pass.getEffect().getUniform(name);
                if (uniform != null) {
                    return uniform;
                }
            }
        }
        return null;
    }

    /**
     * Sets a float uniform in the shader.
     * This method retrieves the uniform by name and sets its value.
     * If the uniform is not found, it logs a warning.
     * @param name - The name of the uniform to set.
     * @param value - The float value to set the uniform to.
     */
    public void setFloatUniform(String name, float value) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(value);
            modifiedUniforms.put("float", new Pair<>(name, value));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", name, getShaderName());
        }
    }

    /**
     * Sets an integer uniform in the shader.
     * This method retrieves the uniform by name and sets its value.
     * If the uniform is not found, it logs a warning.
     * @param name - The name of the uniform to set.
     * @param value - The int value to set the uniform to.
     * @implNote The resetting of int has not been tested yet, so use with caution.
     */
    public void setIntUniform(String name, int value) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(value);
            modifiedUniforms.put("int", new Pair<>(name, value));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", name, getShaderName());
        }
    }

    /**
     * Sets a boolean uniform in the shader.
     * This method retrieves the uniform by name and sets its value.
     * If the uniform is not found, it logs a warning.
     * @param name - The name of the uniform to set.
     * @param value - The boolean value to set the uniform to.
     * @implNote The resetting of boolean has not been tested yet, so use with caution.
     */
    public void setBooleanUniform(String name, boolean value) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(value ? 1 : 0);
            modifiedUniforms.put("bool", new Pair<>(name, value ? 1 : 0));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", name, getShaderName());
        }
    }

    /**
     * Sets a vec2 uniform in the shader.
     * This method retrieves the uniform by name and sets its value.
     * If the uniform is not found, it logs a warning.
     * @param name - The name of the uniform to set.
     * @param x - The x component of the vec2.
     * @param y - The y component of the vec2.
     * @implNote The resetting of vec2 has not been tested yet, so use with caution.
     */
    public void setVec2Uniform(String name, float x, float y) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(x, y);
            modifiedUniforms.put("vec2", new Pair<>(name, new float[]{x, y}));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", name, getShaderName());
        }
    }

    /**
     * Sets a vec3 uniform in the shader.
     * This method retrieves the uniform by name and sets its value.
     * If the uniform is not found, it logs a warning.
     * @param name - The name of the uniform to set.
     * @param x - The x component of the vec3.
     * @param y - The y component of the vec3.
     * @param z - The z component of the vec3.
     * @implNote The resetting of vec3 has not been tested yet, so use with caution.
     */
    public void setVec3Uniform(String name, float x, float y, float z) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(x, y, z);
            modifiedUniforms.put("vec3", new Pair<>(name, new float[]{x, y, z}));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", name, getShaderName());
        }
    }

    /**
     * Sets a vec4 uniform in the shader.
     * This method retrieves the uniform by name and sets its value.
     * If the uniform is not found, it logs a warning.
     * @param name - The name of the uniform to set.
     * @param x - The x component of the vec4.
     * @param y - The y component of the vec4.
     * @param z - The z component of the vec4.
     * @param w - The w component of the vec4.
     * @implNote The resetting of vec4 has not been tested yet, so use with caution.
     */
    public void setVec4Uniform(String name, float x, float y, float z, float w) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(x, y, z, w);
            modifiedUniforms.put("vec4", new Pair<>(name, new float[]{x, y, z, w}));
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", name, getShaderName());
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
        if (!(o instanceof ShaderRenderer)) return false;
        ShaderRenderer that = (ShaderRenderer) o;
        return shaderLocation.equals(that.shaderLocation);
    }

    /**
     * Reapplies modified uniforms to the shader.
     * This method iterates through the modifiedUniforms map and sets each uniform in the shader.
     */
    private void reapplyModifiedUniforms() {
        // Reapply modified uniforms to the new PostChain (because uniforms are not persistent across PostChain instances)
        for (Map.Entry<String, Pair<String, Object>> entry : modifiedUniforms.entrySet()) {
            switch (entry.getKey()) {
                case "float" -> {
                    String uniformName = entry.getValue().getA();
                    float value = (float) entry.getValue().getB();
                    this.setFloatUniform(uniformName, value);
                }
                case "int" -> {
                    String uniformName = entry.getValue().getA();
                    int value = (int) entry.getValue().getB();
                    this.setIntUniform(uniformName, value);
                }
                case "bool" -> {
                    String uniformName = entry.getValue().getA();
                    boolean value = (int) entry.getValue().getB() == 1;
                    this.setBooleanUniform(uniformName, value);
                }
                case "vec2" -> {
                    String uniformName = entry.getValue().getA();
                    float[] vec2Values = (float[]) entry.getValue().getB();
                    this.setVec2Uniform(uniformName, vec2Values[0], vec2Values[1]);
                }
                case "vec3" -> {
                    String uniformName = entry.getValue().getA();
                    float[] vec3Values = (float[]) entry.getValue().getB();
                    this.setVec3Uniform(uniformName, vec3Values[0], vec3Values[1], vec3Values[2]);
                }
                case "vec4" -> {
                    String uniformName = entry.getValue().getA();
                    float[] vec4Values = (float[]) entry.getValue().getB();
                    this.setVec4Uniform(uniformName, vec4Values[0], vec4Values[1], vec4Values[2], vec4Values[3]);
                }
                default -> ModernMayhemMod.LOGGER.warn("Unknown uniform type: {}", entry.getKey());
            }
        }
    }

    /**
     * Resets the ShaderRenderer to its initial state.
     * This method closes the PostChain if it exists,
     * deactivates the shader,
     * and clears the modified uniforms.
     */
    public void reset() {
        if (postChain != null) {
            postChain.close();
            postChain = null;
        }
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
        if (currentEffect != null) {
            System.out.println("Current effect is " + currentEffect.getName());
        } else {
            System.out.println("Current effect is null");
        }
        return currentEffect != null && Objects.equals(currentEffect.getName(), getFullShaderName());
    }
}

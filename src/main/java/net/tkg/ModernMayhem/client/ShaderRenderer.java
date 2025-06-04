package net.tkg.ModernMayhem.client;


import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tkg.ModernMayhem.ModernMayhemMod;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * ShaderRenderer is responsible for managing and rendering a specific shader in Minecraft.
 * It initializes the shader using a PostChain and provides methods to activate, deactivate, and render the shader.
 * This class is designed to be used in the client-side rendering pipeline, specifically for custom shaders.
 * * Note: Ensure that the shader file exists in the specified path and is correctly formatted for Minecraft's shader system.
 * @author CookieG
 */
public class ShaderRenderer {
    private final ResourceLocation shaderLocation;
    private PostChain postChain = null;
    private boolean isActive = false;
    private int lastFrameScreenWidth = -1;
    private int lastFrameScreenHeight = -1;

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

    /**
     * Activate the shader, allowing it to be rendered.
     * Note: You still need to call render() in your rendering loop to see the shader effect.
     */
    public void activate() {
        // We initialize the PostChain here to ensure it is ready when the shader is activated.
        try {
            this.initShader();
            lastFrameScreenWidth = mc.getWindow().getGuiScaledWidth();
            lastFrameScreenHeight = mc.getWindow().getGuiScaledHeight();
            isActive = true;
        } catch (Exception e) {
            ModernMayhemMod.LOGGER.error("Failed to load shader: {}", shaderLocation, e);
        }
    }

    /**
     * Deactivate the shader, preventing it from being rendered.
     * This is useful for performance optimization when the shader is not needed.
     */
    public void deactivate() {
        isActive = false;
        if (postChain != null) {
            postChain.close();
            postChain = null;
        }
    }

    /**
     * Toggles the active state of the shader.
     * If the shader is currently active, it will be deactivated, and vice versa.
     * Note: You still need to call render() in your rendering loop to see the shader effect.
     */
    public void toggleActive() {
        if (isActive) {
            deactivate();
        } else {
            activate();
        }
    }

    /**
     * Renders the shader effect.
     * This method should be called in the rendering loop, typically in the render method of your mod's client-side event handler.
     * Note: If the window size has changed since the last frame, the shader will be deactivated.
     * @param partialTicks - The partial ticks for smooth rendering.
     */
    public void render(float partialTicks) {
        if (!isActive) return;
        if (postChain == null) {
            ModernMayhemMod.LOGGER.warn("Cannot render shader {} because postChain is null", getShaderName());
            return;
        }
        int currentScreenWidth = mc.getWindow().getGuiScaledWidth();
        int currentScreenHeight = mc.getWindow().getGuiScaledHeight();

        if (lastFrameScreenWidth != currentScreenWidth || lastFrameScreenHeight != currentScreenHeight) {
            this.deactivate();
            return;
        }
        postChain.process(partialTicks);

        RenderSystem.setShader(() -> null);
        mc.getMainRenderTarget().bindWrite(false);

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
    }

    /**
     * Gets a specific uniform from the shader.
     * This method searches through the PostChain's passes to find the uniform by name.
     * @param name - The name of the uniform to retrieve.
     * @return The Uniform object if found, or null if not found or if postChain is null.
     */
    public Uniform getUniform(String name) {
        if (postChain == null) {
            ModernMayhemMod.LOGGER.warn("Cannot get uniform {} because postChain is null", name);
            return null;
        }
        try {
            Field passesField = PostChain.class.getDeclaredField("passes");
            passesField.setAccessible(true);
            List<PostPass> passes = (List<PostPass>) passesField.get(postChain);

            for (PostPass pass : passes) {
                if (pass.getName().equals(getShaderName())) {
                    Uniform uniform = pass.getEffect().getUniform(name);
                    if (uniform != null) {
                        return uniform;
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            ModernMayhemMod.LOGGER.error("Failed to access passes field in PostChain", e);
            return null;
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
     */
    public void setIntUniform(String name, int value) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(value);
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
     */
    public void setBooleanUniform(String name, boolean value) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(value ? 1 : 0);
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
     */
    public void setVec2Uniform(String name, float x, float y) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(x, y);
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
     */
    public void setVec3Uniform(String name, float x, float y, float z) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(x, y, z);
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
     */
    public void setVec4Uniform(String name, float x, float y, float z, float w) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(x, y, z, w);
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
     * Initializes the shader by creating a PostChain instance.
     * This method is called when the shader is activated or when the screen size changes.
     * Note: This method will close any existing PostChain instance before creating a new one.
     * @throws IOException if there is an error loading the shader resource.
     */
    private void initShader() throws IOException {
        TextureManager textureManager = mc.getTextureManager();
        ResourceManager resourceManager = mc.getResourceManager();
        RenderTarget renderTarget = mc.getMainRenderTarget();

        if (postChain != null) {
            postChain.close();
            postChain = null;
        }

        postChain = new PostChain(
                textureManager,
                resourceManager,
                renderTarget,
                shaderLocation
        );
        postChain.resize(
                renderTarget.width,
                renderTarget.height
        );
    }
}

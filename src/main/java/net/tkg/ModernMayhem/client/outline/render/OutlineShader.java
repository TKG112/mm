package net.tkg.ModernMayhem.client.outline.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.tkg.ModernMayhem.ModernMayhemMod;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OutlineShader implements AutoCloseable {

    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private Map<String, Integer> uniformLocations = new HashMap<>();
    private Map<String, Integer> textureUnits = new HashMap<>();
    private int nextTextureUnit = 0;

    public OutlineShader(String name) throws IOException {
        this(name, "sobel");
    }

    public OutlineShader(String fragmentName, String vertexName) throws IOException {
        System.out.println("[OutlineShader] Loading shader: " + fragmentName + " (vertex: " + vertexName + ")");

        String vertexSource = loadShaderSource("shaders/outline/" + vertexName + ".vsh");
        System.out.println("[OutlineShader] Loaded vertex shader: " + vertexName + " (" + vertexSource.length() + " chars)");

        String fragmentSource = loadShaderSource("shaders/outline/" + fragmentName + ".fsh");
        System.out.println("[OutlineShader] Loaded fragment shader: " + fragmentName + " (" + fragmentSource.length() + " chars)");

        compile(vertexSource, fragmentSource);
        System.out.println("[OutlineShader] Compiled shader: " + fragmentName);
    }

    private String loadShaderSource(String path) throws IOException {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(ModernMayhemMod.ID, path);
        Resource resource = Minecraft.getInstance().getResourceManager().getResource(location).orElseThrow();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.open(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private void compile(String vertexSource, String fragmentSource) {
        RenderSystem.assertOnRenderThreadOrInit();

        vertexShaderId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexShaderId, vertexSource);
        GL20.glCompileShader(vertexShaderId);

        if (GL20.glGetShaderi(vertexShaderId, GL20.GL_COMPILE_STATUS) == 0) {
            String log = GL20.glGetShaderInfoLog(vertexShaderId);
            throw new RuntimeException("Failed to compile vertex shader:\n" + log);
        }

        fragmentShaderId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentShaderId, fragmentSource);
        GL20.glCompileShader(fragmentShaderId);

        if (GL20.glGetShaderi(fragmentShaderId, GL20.GL_COMPILE_STATUS) == 0) {
            String log = GL20.glGetShaderInfoLog(fragmentShaderId);
            throw new RuntimeException("Failed to compile fragment shader:\n" + log);
        }

        programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShaderId);
        GL20.glAttachShader(programId, fragmentShaderId);

        GL20.glBindAttribLocation(programId, 0, "Position");
        GL20.glBindAttribLocation(programId, 1, "TexCoord");
        GL20.glBindAttribLocation(programId, 1, "TexCoord0");

        GL20.glLinkProgram(programId);

        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
            String log = GL20.glGetProgramInfoLog(programId);
            throw new RuntimeException("Failed to link shader program:\n" + log);
        }
    }

    public void use() {
        RenderSystem.assertOnRenderThreadOrInit();
        GL20.glUseProgram(programId);
    }

    public void setUniform(String name, float value) {
        int location = getUniformLocation(name);
        if (location != -1) {
            GL20.glUniform1f(location, value);
        }
    }

    public void setUniform(String name, int value) {
        int location = getUniformLocation(name);
        if (location != -1) {
            GL20.glUniform1i(location, value);
        }
    }

    public void setUniform(String name, float v1, float v2) {
        int location = getUniformLocation(name);
        if (location != -1) {
            GL20.glUniform2f(location, v1, v2);
        }
    }

    public void setUniform(String name, float v1, float v2, float v3) {
        int location = getUniformLocation(name);
        if (location != -1) {
            GL20.glUniform3f(location, v1, v2, v3);
        }
    }

    public void setUniform(String name, float v1, float v2, float v3, float v4) {
        int location = getUniformLocation(name);
        if (location != -1) {
            GL20.glUniform4f(location, v1, v2, v3, v4);
        }
    }

    public void setTexture(String name, int textureId) {
        int unit = textureUnits.computeIfAbsent(name, k -> nextTextureUnit++);
        int location = getUniformLocation(name);

        if (location != -1) {
            RenderSystem.activeTexture(GL20.GL_TEXTURE0 + unit);
            GlStateManager._bindTexture(textureId);
            GL20.glUniform1i(location, unit);
        }
    }

    private int getUniformLocation(String name) {
        return uniformLocations.computeIfAbsent(name,
                n -> GL20.glGetUniformLocation(programId, n));
    }

    @Override
    public void close() {
        RenderSystem.assertOnRenderThreadOrInit();

        if (programId != 0) {
            GL20.glDeleteProgram(programId);
            programId = 0;
        }

        if (vertexShaderId != 0) {
            GL20.glDeleteShader(vertexShaderId);
            vertexShaderId = 0;
        }

        if (fragmentShaderId != 0) {
            GL20.glDeleteShader(fragmentShaderId);
            fragmentShaderId = 0;
        }

        uniformLocations.clear();
        textureUnits.clear();
    }
}
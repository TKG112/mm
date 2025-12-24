package net.tkg.ModernMayhem.client.outline.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class OutlineFramebuffer {
    public int width;
    public int height;

    private int fboId;
    private int colorTexture;

    private int depthRenderbuffer = 0;
    private int depthTexture = 0;
    private final boolean useDepthTexture;

    public OutlineFramebuffer(int width, int height) {
        this(width, height, false);
    }

    public OutlineFramebuffer(int width, int height, boolean useDepthTexture) {
        this.width = width;
        this.height = height;
        this.useDepthTexture = useDepthTexture;
        create();
    }

    private void create() {
        RenderSystem.assertOnRenderThreadOrInit();

        fboId = GlStateManager.glGenFramebuffers();

        colorTexture = GL30.glGenTextures();
        GlStateManager._bindTexture(colorTexture);
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA8, width, height, 0, GL30.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);

        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D, colorTexture, 0);

        depthRenderbuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthRenderbuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH24_STENCIL8, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL30.GL_RENDERBUFFER, depthRenderbuffer);

        int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
        if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Framebuffer is not complete: " + status);
        }

        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    /**
     * Attaches an external depth source (Renderbuffer or Texture) to this FBO.
     * This allows us to share the Main Minecraft Depth Buffer for occlusion.
     */
    public void attachExternalDepth(int type, int id) {
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);

        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL30.GL_RENDERBUFFER, 0);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL30.GL_TEXTURE_2D, 0, 0);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, 0);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_TEXTURE_2D, 0, 0);

        if (type == GL30.GL_RENDERBUFFER) {
            GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, id);
        } else if (type == GL30.GL_TEXTURE) {
            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_TEXTURE_2D, id, 0);
        }

        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    /**
     * Re-attaches the internal depth buffer. Call this after rendering to clean up.
     */
    public void restoreInternalDepth() {
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);

        if (depthRenderbuffer != 0) {
            GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL30.GL_RENDERBUFFER, depthRenderbuffer);
        }

        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public void bind() {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);
        GlStateManager._viewport(0, 0, width, height);
    }

    public void resize(int newWidth, int newHeight) {
        if (newWidth == width && newHeight == height) return;
        destroy();
        width = newWidth;
        height = newHeight;
        create();
    }

    public void destroy() {
        RenderSystem.assertOnRenderThreadOrInit();
        if (fboId != 0) GlStateManager._glDeleteFramebuffers(fboId);
        if (colorTexture != 0) GlStateManager._deleteTexture(colorTexture);
        if (depthRenderbuffer != 0) GL30.glDeleteRenderbuffers(depthRenderbuffer);
        if (depthTexture != 0) GL30.glDeleteTextures(depthTexture);
        fboId = 0; colorTexture = 0; depthRenderbuffer = 0; depthTexture = 0;
    }

    public int getColorTexture() { return colorTexture; }
    public int getDepthTexture() { return depthTexture; }
    public int getFboId() { return fboId; }
}
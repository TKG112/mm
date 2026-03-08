#version 150

uniform sampler2D GhostColorSampler;
uniform sampler2D GhostDepthSampler;
uniform sampler2D SceneDepthSampler;

uniform float GlobalAlpha;
uniform float DepthBias;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 ghostColor = texture(GhostColorSampler, texCoord);
    if (ghostColor.a <= 0.001) {
        discard;
    }

    float ghostDepth = texture(GhostDepthSampler, texCoord).r;
    float sceneDepth = texture(SceneDepthSampler, texCoord).r;

    // If the ghost is behind the scene, discard it.
    if (sceneDepth < 1.0 && ghostDepth > sceneDepth + DepthBias) {
        discard;
    }

    fragColor = vec4(ghostColor.rgb, ghostColor.a * GlobalAlpha);
}
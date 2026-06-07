#version 150

uniform sampler2D MaskSampler;
uniform sampler2D EntityDepthSampler;
uniform sampler2D SceneDepthSampler;

uniform float IsReversedZ;
uniform vec2  NearFar;
uniform float WorldEpsilon;

in vec2 texCoord;
out vec4 fragColor;

float linStd(float d, float near, float far) {
    float ndc = 2.0 * d - 1.0;
    return (2.0 * near * far) / (far + near - ndc * (far - near));
}

void main() {
    vec4 mask = texture(MaskSampler, texCoord);
    if (mask.a < 0.01) discard;

    float entityDepth = texture(EntityDepthSampler, texCoord).r;
    float sceneDepthRaw = texture(SceneDepthSampler, texCoord).r;

    float entityDist = linStd(entityDepth, NearFar.x, NearFar.y);

    float sceneDepthStd = (IsReversedZ > 0.5) ? (1.0 - sceneDepthRaw) : sceneDepthRaw;
    float sceneDist = linStd(sceneDepthStd, NearFar.x, NearFar.y);

    if (sceneDist < entityDist - WorldEpsilon) discard;

    fragColor = mask;
}
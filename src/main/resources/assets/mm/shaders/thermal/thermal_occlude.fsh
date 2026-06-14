#version 150

uniform sampler2D MaskSampler;
uniform sampler2D EntityDepthSampler;
uniform sampler2D SceneDepthSampler;

uniform vec2  InSize;
uniform float IsReversedZ;
uniform vec2  NearFar;
uniform float WorldEpsilon;

in vec2 texCoord;
out vec4 fragColor;

float linearize(float d) {
    float n = NearFar.x;
    float f = NearFar.y;
    float zndc = d * 2.0 - 1.0;
    return (2.0 * n * f) / (f + n - zndc * (f - n));
}

void main() {
    vec4 mask = texture(MaskSampler, texCoord);
    if (mask.a < 0.01) discard;

    float entityZ  = texture(EntityDepthSampler, texCoord).r;
    float sceneRaw = texture(SceneDepthSampler,  texCoord).r;
    float sceneZ   = (IsReversedZ > 0.5) ? (1.0 - sceneRaw) : sceneRaw;

    float entityLin = linearize(entityZ);
    float sceneLin  = linearize(sceneZ);

    if (sceneLin < entityLin - WorldEpsilon) discard;

    fragColor = mask;
}
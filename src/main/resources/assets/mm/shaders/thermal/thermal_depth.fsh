#version 150

uniform sampler2D BlurSampler;
uniform sampler2D MaskSampler;
uniform sampler2D MaskDepthSampler;

uniform vec2 InSize;

in vec2 texCoord;

void main() {
    float blurA = texture(BlurSampler, texCoord).a;
    float maskA = texture(MaskSampler, texCoord).a;
    float outlineA = clamp(blurA - maskA, 0.0, 1.0);
    if (outlineA < 0.05) discard;

    vec2 oneTexel = 1.0 / InSize;
    float minDepth = 2.0;

    for (int dx = -4; dx <= 4; dx++) {
        for (int dy = -4; dy <= 4; dy++) {
            vec2 uv = texCoord + vec2(float(dx), float(dy)) * oneTexel;
            if (texture(MaskSampler, uv).a > 0.1) {
                float d = texture(MaskDepthSampler, uv).r;
                if (d < minDepth) minDepth = d;
            }
        }
    }

    if (minDepth > 1.5) discard;

    gl_FragDepth = minDepth;
}
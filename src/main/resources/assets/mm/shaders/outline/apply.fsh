#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D GlowSampler;

uniform vec4 OutlineColor;
uniform float FillAlpha;
uniform float OutlineAlpha;
uniform float GlowStrength;
uniform int Mode; // 0=off, 1=outline, 2=fill, 3=fill+outline

in vec2 texCoord;
out vec4 fragColor;

void main() {
    float body = texture(DiffuseSampler, texCoord).a;
    float glow = texture(GlowSampler, texCoord).a;

    float fill = body * FillAlpha;
    float outline = max(glow - body, 0.0) * OutlineAlpha * GlowStrength;

    float alpha = 0.0;

    if (Mode == 1) {
        alpha = outline;
    } else if (Mode == 2) {
        alpha = fill;
    } else if (Mode == 3) {
        alpha = max(fill, outline);
    } else {
        discard;
    }

    if (alpha <= 0.001) {
        discard;
    }

    fragColor = vec4(OutlineColor.rgb, alpha * OutlineColor.a);
}
#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 InSize;
uniform vec2 OutSize;
uniform int Horizontal;
uniform float Radius;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 texel = 1.0 / InSize;
    vec2 dir = (Horizontal == 1) ? vec2(texel.x, 0.0) : vec2(0.0, texel.y);

    vec4 sum = vec4(0.0);
    float total = 0.0;

    int radius = int(max(1.0, Radius));

    for (int i = -radius; i <= radius; i++) {
        float x = float(i);
        float w = exp(-(x * x) / (2.0 * Radius * Radius));
        vec4 sampleColor = texture(DiffuseSampler, texCoord + dir * x);
        sum += sampleColor * w;
        total += w;
    }

    fragColor = sum / max(total, 0.0001);
}
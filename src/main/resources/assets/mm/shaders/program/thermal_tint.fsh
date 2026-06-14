#version 150

uniform sampler2D DiffuseSampler;
uniform float Resolution;
uniform vec3  Gray;
uniform vec3  TintColor;
uniform float InvertWorld;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec3 c = texture(DiffuseSampler, texCoord).rgb;
    c = c - fract(c * Resolution) / Resolution;
    float luma = dot(c, Gray);
    if (InvertWorld > 0.5) luma = 1.0 - luma;
    fragColor = vec4(luma * TintColor, 1.0);
}

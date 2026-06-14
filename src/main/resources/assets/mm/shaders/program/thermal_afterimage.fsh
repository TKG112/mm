#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D PrevSampler;
uniform vec3 Phosphor;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 c = texture(DiffuseSampler, texCoord);
    vec4 prev = texture(PrevSampler, texCoord);
    c.rgb = max(prev.rgb * Phosphor, c.rgb);
    c.a = 1.0;
    fragColor = c;
}

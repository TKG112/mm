#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 InSize;
uniform vec2 OutSize;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 src = texture(DiffuseSampler, texCoord);

    float alpha = max(src.a, max(src.r, max(src.g, src.b)));
    if (alpha <= 0.001) {
        discard;
    }

    fragColor = vec4(src.rgb, alpha);
}
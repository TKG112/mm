#version 150

uniform sampler2D DiffuseSampler;
uniform vec4 OutlineColor;
uniform int UseSourceColor;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 blur = texture(DiffuseSampler, texCoord);

    vec3 finalColor = OutlineColor.rgb;

    if (UseSourceColor == 1) {
        finalColor = blur.rgb;
    }

    fragColor = vec4(finalColor, blur.a * OutlineColor.a);
}
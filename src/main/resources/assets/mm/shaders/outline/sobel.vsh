#version 150

in vec2 Position;
in vec2 TexCoord;

uniform vec2 InSize;
uniform vec2 OutSize;

out vec2 texCoord;
out vec2 oneTexel;

void main() {
    vec4 outPos = vec4(Position, 0.0, 1.0);
    gl_Position = outPos;

    oneTexel = 1.0 / InSize;
    texCoord = TexCoord;
}
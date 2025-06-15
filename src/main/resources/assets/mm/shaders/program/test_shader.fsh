#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoordAA;

uniform float BloomThreshold;

out vec4 fragColor;

const vec3 luminanceWeights = vec3(0.299, 0.587, 0.114);

// Extracts bright areas from the texture
// minimum brightness level can be adjusted with BloomThreshold
void main() {
    vec4 texColor = texture(DiffuseSampler, texCoordAA);
    float brightness = dot(texColor.rgb, luminanceWeights);

    if (brightness >= BloomThreshold)
        fragColor  = vec4(0.0, 1.0, 0.0, 1.0); // Bright areas are green (for testing)
    else
        fragColor  = vec4(1.0, 0.0, 0.0, 1.0); // Dark areas are red (for testing)
}

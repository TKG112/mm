#version 150

uniform sampler2D DiffuseSampler; // Texture sampler for the diffuse texture

in vec2 Position;// Texture coordinate from vertex shader

uniform float BloomThreshold; // Threshold for bloom effect

out vec4 fragColor; // Output color of the fragment shader

// Each post shaders are fragment shaders
// This means that each pixel will be processed inividually by this shader (one pixel at a time)
void main() {

    // Get the pixel color at the texture coordinate
    vec4 texColor = texture(DiffuseSampler, Position);

    // Calculate brightness using the luminance formula
    float brightness = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));

    if (brightness >= BloomThreshold) { // If brightness is above the threshold, set color to white
        fragColor = vec4(1.0, 1.0, 1.0, 1.0);
    } else { // Otherwise, set color to black
        fragColor = vec4(0.0, 0.0, 0.0, 1.0);
    }
}
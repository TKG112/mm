#version 150

// Stamps the first-person hand into the goggle entity mask as a warm body.
//
// Called right after the engine draws the hand, when the hand is by far the closest thing in the main
// framebuffer. We treat any pixel nearer than HandMaxDist (linear, world units) as the hand and output
// a hot value with a little luminance detail; everything else is discarded so the existing entity mask
// shows through. The goggle chain then runs this through its palette (TVG) / highlight (NVG).
//
// Standard Z only (the vanilla path). Under a shaderpack the hand reads from the gbuffer instead, which
// is handled separately.

uniform sampler2D MainSampler;       // main colour (world + hand)
uniform sampler2D SceneDepthSampler; // main depth - the hand is the nearest surface
uniform vec2  NearFar;               // x = near, y = far (world units)
uniform float HandMaxDist;           // nearer than this (world units) = the hand

in vec2 texCoord;
out vec4 fragColor;

float linearize(float d) {
    float n = NearFar.x;
    float f = NearFar.y;
    float zndc = d * 2.0 - 1.0;
    return (2.0 * n * f) / (f + n - zndc * (f - n));
}

void main() {
    float d = texture(SceneDepthSampler, texCoord).r;
    if (d >= 1.0) discard;                 // cleared / nothing here
    if (linearize(d) > HandMaxDist) discard; // too far to be the hand

    vec3 c = texture(MainSampler, texCoord).rgb;
    float luma = dot(c, vec3(0.299, 0.587, 0.114));

    // A hand is a warm body: bias hot, keep a touch of detail so fingers/edges read.
    float heat = mix(0.82, 1.0, clamp(luma * 1.4, 0.0, 1.0));
    fragColor = vec4(vec3(heat), 1.0);
}

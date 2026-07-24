#version 150

// Depth-aware carve pass.
//
// The held item is a cold object that should block entity heat BEHIND it. Earlier this pass was pure
// screen-space: it zeroed the mask wherever the item silhouette landed, which silently assumed the item is
// always the frontmost surface. Under a shaderpack an entity can clip CLOSER than the viewmodel (Iris hand
// depth), so part of the entity is genuinely in front of the item - yet the old carve still zeroed it, eating
// heat that should show. This version compares depths and carves ONLY where the item is actually in front.
//
// CoverageSampler   : item silhouette (solid white where the item is), rendered under the hand projection.
// ItemDepthSampler  : item depth, same hand projection  -> linearised with ItemNearFar.
// EntityDepthSampler: entity depth (maskTarget), world projection -> linearised with EntityNearFar.
// Drawn over occludedMaskTarget with blend DISABLED and depth writes OFF: covered+frontmost -> write zero;
// everything else -> discard (mask untouched).

uniform sampler2D CoverageSampler;
uniform sampler2D ItemDepthSampler;
uniform sampler2D EntityDepthSampler;
uniform vec2  ItemNearFar;     // (viewmodelNear, viewmodelFar)
uniform vec2  EntityNearFar;   // (maskNear, maskFar)
uniform float IsReversedZItem;   // item/coverage depth: captured normal-Z (range-flipped in renderViewmodelCoverage) -> 0.0
uniform float IsReversedZEntity; // entity/mask depth: pipeline convention (reversed-Z under a shaderpack)
uniform float DepthBias;       // slack (world units) so near-equal depths still carve (bias toward "item front")
uniform float DepthAware;      // 1.0 under a shaderpack (compare depths); 0.0 in vanilla (always carve under silhouette)

in vec2 texCoord;
out vec4 fragColor;

// Linearise a depth-buffer value to a POSITIVE eye-space distance.
// IMPORTANT: this must use the SAME convention as thermal_occlude.fsh. If your occlude shader has its own
// linearise helper, paste its body in here verbatim so the two passes agree (the reversed-Z branch is the
// most likely place a sign needs flipping).
float linEye(float d, vec2 nf, float reversed) {
    if (reversed > 0.5) d = 1.0 - d;
    float z = d * 2.0 - 1.0;                                  // window depth -> NDC [-1, 1]
    return (2.0 * nf.x * nf.y) / (nf.y + nf.x - z * (nf.y - nf.x));
}

void main() {
    vec4 c = texture(CoverageSampler, texCoord);
    float cov = max(max(c.r, c.g), max(c.b, c.a));
    if (cov < 0.02) discard;                                  // not the item -> leave heat as-is

    // Vanilla draws the real hand on top of the finished thermal image, so plain screen-space carving is
    // correct there - and vanilla depth values don't share the shaderpack convention. Only run the depth
    // compare when a pack is active, where an entity can clip closer than the viewmodel.
    if (DepthAware > 0.5) {
        float itemEye   = linEye(texture(ItemDepthSampler,   texCoord).r, ItemNearFar,   IsReversedZItem);
        float entityEye = linEye(texture(EntityDepthSampler, texCoord).r, EntityNearFar, IsReversedZEntity);
        // Entity meaningfully closer than the item -> it is the real foreground (poking through) -> keep heat.
        if (entityEye < itemEye - DepthBias) discard;
    }

    // Item is the frontmost surface (entity behind it / empty world behind it), or we are in vanilla -> cold.
    fragColor = vec4(0.0, 0.0, 0.0, 0.0);
}

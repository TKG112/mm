#version 150

// Mosaic / pixelate the whole composited image (world + thermal entity).
uniform sampler2D DiffuseSampler;
uniform vec2  InSize;
uniform float MosaicSize;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 mosaicInSize = InSize / MosaicSize;
    vec2 fractPix = fract(texCoord * mosaicInSize) / mosaicInSize;
    vec2 pixelatedCoord = texCoord - fractPix;
    vec4 c = texture(DiffuseSampler, pixelatedCoord);
    c.a = 1.0;
    fragColor = c;
}

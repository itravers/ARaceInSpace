#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

#define BLUR 1.0

varying vec2 vTexCoord0;
varying LOWP vec4 vColor;

uniform sampler2D u_texture;

uniform float clip_rotation;
uniform vec2 clip_pos;
uniform vec2 clip_size;

float clip(vec2 p) {
  float c = cos(-clip_rotation);
	float s = sin(-clip_rotation);

	//unrotate rectangle
	float rx = clip_pos.x + c * (p.x - clip_pos.x) - s * (p.y - clip_pos.y);
	float ry = clip_pos.y + s * (p.x - clip_pos.x) + c * (p.y - clip_pos.y);

	//determine rectangle
	vec2 v = abs(vec2(rx, ry) - clip_pos - clip_size/2.) - clip_size/2. + 0.5;
	float d = length(max(v, 0.0));
	return smoothstep(BLUR, 0.0, d);
}

void main() {
	vec4 texColor0 = texture2D(u_texture, vTexCoord0);
	texColor0.a *= clip(gl_FragCoord.xy);
	gl_FragColor = vColor * texColor0;
}
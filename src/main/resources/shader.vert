#version 330 core

layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec2 in_TextureCoord;

out vec2 pass_TextureCoord;

uniform mat4 u_mvp;

void main(void) {
	gl_Position = u_mvp * vec4(in_Position, 1.0);

	pass_TextureCoord = in_TextureCoord;
}
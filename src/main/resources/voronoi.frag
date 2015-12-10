#version 330
out vec4 out_color;

uniform vec2 seeds[32];
uniform vec3 colors[32];
uniform int seeds_count;

void main() {
    float dist = distance(seeds[0], gl_FragCoord.xy);
    vec3 color = colors[0];
    for (int i = 1; i < seeds_count; ++i) {
        float current = distance(seeds[i], gl_FragCoord.xy);
        if (current < dist) {
            color = colors[i];
            dist = current;
        }
    }

    out_color = vec4(color, 1.0);
}
#version 330
out vec4 fragColor;

uniform vec2 seeds[32];
uniform vec3 colors[32];
uniform int seeds_count;

vec3 getColor() {
    float dist = distance(seeds[0], gl_FragCoord.xy);
    vec3 color = colors[0];
    for (int i = 1; i < seeds_count; ++i) {
        float current = distance(seeds[i], gl_FragCoord.xy);
        if (current < dist) {
            color = colors[i];
            dist = current;
        }
    }

    if (dist < 5.0) {
        color = vec3(0.0, 0.0, 0.0);
    }

    return color;
}

void main() {
    vec3 color = getColor();

    fragColor = vec4(color, 1.0);
}
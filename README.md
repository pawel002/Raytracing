# Raytracing
This repository contains a second project for object programming course at AGH university in Kraków 22/23. 
## Description
The application was written entirely in Java and Swing and can be run using Gradle. The main focus of this project was implementing some of the real world's light properties using basic algebra rather than focusing on optimization and usage of GPU. This makes the program massively inefficient, although it creates satisfying results for small numbers of casted rays. The vieport allows the user to set up a scene, which then can be rendered into a picture with given resolution. List of implemented things:

Objects:
- Parametric equations for sphere, plane and torus (ray-torus intersection solved using 4th degree polynomial solver).
- Class that represents a triangle.
- Class that represents a parallelepiped created from 8 traingles.
- Reading a mesh from .obj file and computing it's bouding sphere using AABB and fiding a sphera that contains it.

Surface properties:
- Reflection coefficient.
- Roughness coefficient.
- Albedo (self emission) coefficient.
- Lambertian coefficient.
- Blinn-Phong coefficient - explained in the light properties.
- Blinn-Phong exponent - explained in the light properties.
- Texture mapping using equirectangular projection for spheres and baricentric coordinates for triangles.
- An object can be transparent with a given refractive index.
- The entire scene is wrapped around a skybox sampled using equirectangular projection.

Light properties:
- Color is represented by a Vecd3 class which holds three doule values and is clipped to (0,1).
- Simulation supports [Lambertian](https://en.wikipedia.org/wiki/Lambertian_reflectance) reflectance for all objects.
- Simulation supports [Blinn-Phong](https://en.wikipedia.org/wiki/Blinn%E2%80%93Phong_reflection_model) light reflection model with variable exponent.
- Light gets refracted or reflected object depending on [Schlick's approximation](https://en.wikipedia.org/wiki/Schlick%27s_approximation).
- Light intensity drops with inverse of square of distance from given point.
- The system of "progressive rendering" which after given button is pressed, averages all rendered frames over time, to converge to final image in real time.

Light Sources:
- Point light with variable position and intesity.
- Directional light with variable direction and constant intensity along this direction.
- Spherical light with variable position and shadow can be used in creating a soft shadow effect. However, it requires many samples to converge to noisless soft shadow.

Interface:
- Variable vieport resolution.
- Variable mouse sensitivity.
- Variable camera movement speed.
- Variable max number of reflections of rays projected into the scene.
- Variable field of view.
- Few skybox presets.
- Few scene presets.
- Variables describing output image such as resolution, max ray depth and number of samples per pixel (aliasing).
- Button which can be used to clear the scene (it should clear automatically, but sometimes it does not) and a render button.
- By pressing T, Y, R user can resepectively actiave progressive rendering, turn it off and reset the buffer.

## Renders
All renders are available as scenes.

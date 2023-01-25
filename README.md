# Raytracing
This repository contains a second project for object programming course at AGH university in Kraków 22/23. 
## Description
The application was written entirely in Java and Swing and can be run using Gradle. The main focus of this project was implementing some of the real world's light properties using basic algebra rather than focusing on optimization and usage of GPU. This makes the program massively inefficient, although it creates satisfying results for small numbers of casted rays. List of implemented things:

Objects:
- Parametric equations for sphere, plane and torus (ray-torus intersection solved using 4th degree polynomial solver).
- Class that represents a triangle.
- Class that represents a parallelepiped created from 8 traingles.
- Reading a mesh from .obj file and computing it's bouding sphere using AABB and fiding a sphera that contains it.

Surface properties:
- Reflection coefficient.
- Roughness coefficient.
- Albedo (self emission) coefficient.
- Lambertian constant.
- Blinn-Phong coefficient - explained in the light properties.
- Blinn-Phong exponent - explained in the light properties.
- Texture mapping using equirectangular projection for spheres and baricentric coordinates for triangles.
- An object can be transparent with a given refractive index.

Light properties:
- Color is represented by a Vecd3 class which holds three doule values and is clipped to (0,1).
- Simulation supports lambertian shading for any objects.
- Simulation supports Blinn-Phong light reflection model with variable exponent.
- Light gets refracted or reflected object depending on [Schlick's approximation](https://en.wikipedia.org/wiki/Schlick%27s_approximation).

# Raytracing

This repository contains my second project for the object programming course at AGH university in Kraków 22/23. 

## Description

The application was written entirely in Java and Swing and can be run using Gradle. The main focus of this project was implementing some of the real world's light properties using basic algebra rather than focusing on optimization and usage of GPU. This makes the program massively inefficient, although it creates satisfying results for small numbers of casted rays. The vieport allows the user to set up a scene, which then can be rendered into a picture with given resolution. List of implemented things:



Objects:

- Parametric equations for sphere, plane and torus (ray-torus intersection solved using 4th degree polynomial solver).

- Class that represents a triangle.

- Class that represents a parallelepiped created from 8 triangles.

- Reading a mesh from .obj (from src/main/resources/meshes) file and computing its building sphere using AABB and finding a sphera that contains it.



Surface properties:

- Reflection coefficient.

- Roughness coefficient. For accurate scattering, more samples are needed.

- Albedo (self emission) coefficient.

- Lambertian coefficient.

- Blinn-Phong coefficient - explained in the light properties.

- Blinn-Phong exponent - explained in the light properties.

- Texture mapping using equirectangular projection for spheres and baricentric coordinates for triangles.

- An object can be transparent with a given refractive index.

- The entire scene is wrapped around a skybox sampled using equirectangular projection.



Light properties:

- Color is represented by a Vecd3 class which holds three double values and is clipped to (0,1).

- Simulation supports [Lambertian](https://en.wikipedia.org/wiki/Lambertian_reflectance) reflectance for all objects.

- Simulation supports [Blinn-Phong](https://en.wikipedia.org/wiki/Blinn%E2%80%93Phong_reflection_model) light reflection model with variable exponent.

- Light gets refracted or reflected object depending on [Schlick's approximation](https://en.wikipedia.org/wiki/Schlick%27s_approximation). For accurate reflections, more samples are needed.

- Light intensity drops with the inverse of the square of distance from a given point.

- The system of "progressive rendering" which, after a given button is pressed, averages all rendered frames over time, to converge to the final image in real time.



Light Sources:

- Point light with variable position and intensity.

- Directional light with variable direction and constant intensity in this direction.

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

- By pressing T, Y, R user can respectively activate progressive rendering, turn it off and reset the buffer.



Users can load a custom scene using the file called "custom.txt" placed in src/main/resources/customScene and editing values. To render this scene, one can simply select "custom" in the scene selector. The "/" character makes the parser skip this line.


## Usage
Using the program is pretty straight forward, as everything is well described and user friendly. Below you can see a screenshot of working simulation.
![app](https://github.com/pawel002/Raytracing/blob/master/renders/app.png)
By pressing on the viewport the cursor will disappear and user will be able to move around in the scene. To Exit this mode you can press Esc. All the function in the settings tab work as described by label. When needed, use clear the scene button.

## Renders
All renders are available as scenes.
![mesh](https://github.com/pawel002/Raytracing/blob/master/renders/mesh.png)
![cornell](https://github.com/pawel002/Raytracing/blob/master/renders/cornell.png)
![donut](https://github.com/pawel002/Raytracing/blob/master/renders/donutspace.png)
![phong](https://github.com/pawel002/Raytracing/blob/master/renders/phong.png)
![reflect](https://github.com/pawel002/Raytracing/blob/master/renders/reflect.png)
![refract](https://github.com/pawel002/Raytracing/blob/master/renders/refract.png)
![rough](https://github.com/pawel002/Raytracing/blob/master/renders/rough.png)
![textures](https://github.com/pawel002/Raytracing/blob/master/renders/textures.png)
![balls](https://github.com/pawel002/Raytracing/blob/master/renders/balls.png)

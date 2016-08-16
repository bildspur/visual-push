// Draws a triangle using low-level OpenGL calls.

import java.nio.*;

PShader sh;

float[] attribs;

FloatBuffer attribBuffer;

int attribVboId;

final static int VERT_CMP_COUNT = 4; // vertex component count (x, y, z, w) -> 4
final static int CLR_CMP_COUNT = 4;  // color component count (r, g, b, a) -> 4

public void setup() {
  size(640, 360, P3D);

  // Loads a shader to render geometry w/out
  // textures and lights.
  sh = loadShader("frag.glsl", "vert.glsl");

  attribs = new float[24];
  attribBuffer = allocateDirectFloatBuffer(24);

  PGL pgl = beginPGL();

  IntBuffer intBuffer = IntBuffer.allocate(1);
  pgl.genBuffers(1, intBuffer);

  attribVboId = intBuffer.get(0);

  endPGL();
}

public void draw() {

  PGL pgl = beginPGL();

  background(0);

  // The geometric transformations will be automatically passed
  // to the shader.
  rotate(frameCount * 0.01f, width, height, 0);

  updateGeometry();
  sh.bind();

  // get "vertex" attribute location in the shader
  final int vertLoc = pgl.getAttribLocation(sh.glProgram, "vertex");
  // enable array for "vertex" attribute
  pgl.enableVertexAttribArray(vertLoc);

  // get "color" attribute location in the shader
  final int colorLoc = pgl.getAttribLocation(sh.glProgram, "color");
  // enable array for "color" attribute
  pgl.enableVertexAttribArray(colorLoc);


  /*
    BUFFER LAYOUT from updateGeometry()

    xyzwrgbaxyzwrgbaxyzwrgba...

    |v1       |v2       |v3       |...
    |0   |4   |8   |12  |16  |20  |...
    |xyzw|rgba|xyzw|rgba|xyzw|rgba|...

    stride (values per vertex) is 8 floats
    vertex offset is 0 floats (starts at the beginning of each line)
    color offset is 4 floats (starts after vertex coords)

       |0   |4   |8
    v1 |xyzw|rgba|
    v2 |xyzw|rgba|
    v3 |xyzw|rgba|
       |...
   */
  final int stride       = (VERT_CMP_COUNT + CLR_CMP_COUNT) * Float.BYTES;
  final int vertexOffset =                                0 * Float.BYTES;
  final int colorOffset  =                   VERT_CMP_COUNT * Float.BYTES;

  // bind VBO
  pgl.bindBuffer(PGL.ARRAY_BUFFER, attribVboId);
  // fill VBO with data
  pgl.bufferData(PGL.ARRAY_BUFFER, Float.BYTES * attribs.length, attribBuffer, PGL.DYNAMIC_DRAW);
  // associate currently bound VBO with "vertex" shader attribute
  pgl.vertexAttribPointer(vertLoc, VERT_CMP_COUNT, PGL.FLOAT, false, stride, vertexOffset);
  // associate currently bound VBO with "color" shader attribute
  pgl.vertexAttribPointer(colorLoc, CLR_CMP_COUNT, PGL.FLOAT, false, stride, colorOffset);
  // unbind VBO
  pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);


  pgl.drawArrays(PGL.TRIANGLES, 0, 3);


  // disable arrays for attributes before unbinding the shader
  pgl.disableVertexAttribArray(vertLoc);
  pgl.disableVertexAttribArray(colorLoc);

  sh.unbind();

  endPGL();
}

// Triggers a crash when closing the output window using the close button
//public void dispose() {
//  PGL pgl = beginPGL();

//  IntBuffer intBuffer = IntBuffer.allocate(1);
//  intBuffer.put(attribVboId);
//  intBuffer.rewind();
//  pgl.deleteBuffers(1, intBuffer);

//  endPGL();
//}

void updateGeometry() {
  // Vertex 1
  attribs[0] = 0;
  attribs[1] = 0;
  attribs[2] = 0;
  attribs[3] = 1;

  // Color 1
  attribs[4] = 1;
  attribs[5] = 0;
  attribs[6] = 0;
  attribs[7] = 1;

  // Vertex 2
  attribs[8] = width/2;
  attribs[9] = height;
  attribs[10] = 0;
  attribs[11] = 1;

  // Color 2
  attribs[12] = 0;
  attribs[13] = 1;
  attribs[14] = 0;
  attribs[15] = 1;

  // Vertex 3
  attribs[16] = width;
  attribs[17] = 0;
  attribs[18] = 0;
  attribs[19] = 1;

  // Color 3
  attribs[20] = 0;
  attribs[21] = 0;
  attribs[22] = 1;
  attribs[23] = 1;

  attribBuffer.rewind();
  attribBuffer.put(attribs);
  attribBuffer.rewind();
}

FloatBuffer allocateDirectFloatBuffer(int n) {
  return ByteBuffer.allocateDirect(n * Float.BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
}
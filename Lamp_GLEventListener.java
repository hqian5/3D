package mainAssignment;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import gmaths.*;

import javax.naming.Name;
import java.util.Random;

public class Lamp_GLEventListener implements GLEventListener {

    public Lamp_GLEventListener(Camera camera){
        this.camera = camera;
        this.camera.setPosition(new Vec3(0f, 8f, 18f));
        this.camera.setTarget(new Vec3(0f, 2f, 0f));
    }

    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LESS);
        gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
        gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
        gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
        initialise(gl);
        startTime = getSeconds();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glViewport(x, y, width, height);
        float aspect = (float)width/(float)height;
        camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
    }

    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        light.dispose(gl);
        board.dispose(gl);
    }

    public void display(GLAutoDrawable drawable){
        GL3 gl = drawable.getGL().getGL3();
        render(gl);
    }

    public void rotateArm(){
        Random random = new Random();
        rotateAngle1 = random.nextInt(90) - 30;
        rotateAngle2 = random.nextInt(180) - 90;
        lowerArmRotate.setTransform(Mat4Transform.rotateAroundZ(rotateAngle1));
        lowerArmRotate.update();
        upperArmRotate.setTransform(Mat4Transform.rotateAroundZ(rotateAngle2));
        upperArmRotate.update();
    }

    public void lampJump(){
        Random random = new Random();
        xPosition = random.nextInt(10) - 5;
        lampTransform.setTransform(Mat4Transform.translate(xPosition, 0, 0));
        lampTransform.update();
    }

    public void shakeHead(){
        Random random = new Random();
        rotateAngle3 = random.nextInt(180) - 90;
        lampHeadRotate.setTransform(Mat4Transform.rotateAroundY(rotateAngle3));
        lampHeadRotate.update();
    }

    private Camera camera;
    private Light light;
    private Model floor, wall1, wall2, board, leg1, leg2, leg3, leg4, phone, cube, sphere;
    private SGNode lamp;

    private TransformNode lampTransform, lowerArmRotate, upperArmRotate, lampHeadRotate;
    private float xPosition = 0, rotateAngle1 = 0, rotateAngle2 = 0, rotateAngle3;
    public void initialise(GL3 gl){

        int[] textureId1 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\board3.jpg");
        int[] textureId2 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\floor.jpg");
        int[] textureId3 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\leg1.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\phone.jpg");
        int[] textureId5 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\wall.jpg");
        int[] textureId6 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\window1.jpg", GL.GL_TEXTURE_MIN_FILTER, GL.GL_TEXTURE_MIN_FILTER,
                                                      GL.GL_LINEAR, GL.GL_LINEAR);
        int[] textureId7 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\base.jpg");
        int[] textureId8 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\arm.jpg");

        light = new Light(gl);
        light.setCamera(camera);

        float legHeight = 3.5f;
        float boardHeight = legHeight + 3f;
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        Material material = new Material(new Vec3(0.5f, 0.1f, 0.2f), new Vec3(0.5f, 0.1f, 0.2f), new Vec3(0.75f, 0.75f, 0.75f), 64.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(10f, 0.5f, 6f), Mat4Transform.translate(0, boardHeight, 0));
        board = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId1);

        float size = 16f;
        mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_tt_05.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_tt_05.txt");
        material = new Material(new Vec3(1, 1, 1), new Vec3(1, 1, 1), new Vec3(0.3f, 0.3f, 0.3f ), 32.0f);
        modelMatrix = Mat4Transform.scale(size, 1f, size);
        floor = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId2);

        mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_tt_05.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_tt_05.txt");
        material = new Material(new Vec3(1, 1, 1), new Vec3(1, 1, 1), new Vec3(0.45f, 0.45f, 0.45f ), 32.0f);
        modelMatrix = Mat4Transform.scale(size, 1f, size);
        wall1 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId6);

        mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_tt_05.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_tt_05.txt");
        material = new Material(new Vec3(1,1,1), new Vec3(1, 1, 1), new Vec3(0.45f, 0.45f, 0.45f), 32.0f);
        modelMatrix = Mat4Transform.scale(size,1f, size);
        wall2 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId5);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.6f, 0.6f, 0.6f), 56.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(1.0f, legHeight, 1.0f), Mat4Transform.translate(4.5f, 0.5f, 2.5f));
        leg1 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.6f, 0.6f, 0.6f), 56.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(1.0f, legHeight, 1.0f), Mat4Transform.translate(-4.5f, 0.5f, 2.5f));
        leg2 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.6f, 0.6f, 0.6f), 56.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(1.0f, legHeight, 1.0f), Mat4Transform.translate(4.5f, 0.5f, -2.5f));
        leg3 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.6f, 0.6f, 0.6f), 56.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(1.0f, legHeight, 1.0f), Mat4Transform.translate(-4.5f, 0.5f, -2.5f));
        leg4 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.75f, 0.5f, 0.2f), new Vec3(0.5f, 0.75f, 0.3f), new Vec3(0.75f, 0.5f, 0.65f), 56.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(0.45f, 0.05f, 0.75f), Mat4Transform.translate(-3.5f, 70.5f, 3.0f));
        phone = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId4);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.8f, 0.75f, 0.6f), new Vec3(0.75f,0.25f, 0.5f), new Vec3(0.5f, 0.5f, 0.5f), 64.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(4f, 4f, 4f), Mat4Transform.translate(0, 0.5f, 0));
        cube = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId7);

        mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.75f, 0.5f, 0.2f), new Vec3(0.5f, 0.75f, 0.3f), new Vec3(0.75f, 0.5f, 0.65f), 64.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(3f, 3f, 3f), Mat4Transform.translate(0, 0.5f, 0));
        sphere = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId8);

        float basementHeight = 0.5f;
        float armHeight = 1.5f;
        float jointDia = 0.5f;
        float headLength = 1.0f;
        float headWidth = 0.5f;
        float headHeight = 0.5f;

        lamp = new NameNode("lamp");

        Random random = new Random();
        xPosition = random.nextInt(10) - 5;
        lampTransform = new TransformNode("lamp rotates", Mat4Transform.translate(xPosition, 0, 0));

        TransformNode lampUp = new TransformNode("lamp translate to the top of table", Mat4Transform.translate(0, 3.5f, 0));

        NameNode basement = new NameNode("basement");
        Mat4 m = Mat4Transform.scale(2.5f, basementHeight, 0.75f);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f,0));
        TransformNode basementTransform = new TransformNode("basement transform", m);
        ModelNode basementShape = new ModelNode("cube(basement)", cube);

        rotateAngle1 = random.nextInt(90) - 30;
        NameNode lowerArm = new NameNode("lowerArm");
        TransformNode lowerArmUp = new TransformNode("lowerArm translates to the top of basement", Mat4Transform.translate(0,  basementHeight,0));
        lowerArmRotate = new TransformNode("lower arm rotates", Mat4Transform.rotateAroundZ(rotateAngle1));
        m = new Mat4(1);
        m = Mat4.multiply(m,Mat4Transform.scale(0.5f, armHeight, 0.5f));
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode lowerArmMove = new TransformNode("final movement", m);
        ModelNode lowerArmShape = new ModelNode("shape of lowerArm", sphere);

        NameNode middleJoint = new NameNode("middleJoint");
        TransformNode middleJointUp = new TransformNode("Joint translates to the top of lowerArm", Mat4Transform.translate(0, armHeight, 0));
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(jointDia, jointDia, jointDia));
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode middleJointMove = new TransformNode("final movement", m);
        ModelNode middleJointShape = new ModelNode("shape of Joint", sphere);

        rotateAngle2 = random.nextInt(180) - 90;
        NameNode upperArm = new NameNode("upperArm");
        TransformNode upperArmUp = new TransformNode("upperArm translates to the top of middleJoint", Mat4Transform.translate(0, armHeight+jointDia, 0));
        upperArmRotate = new TransformNode("upper arm rotates", Mat4Transform.rotateAroundZ(rotateAngle2));
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(0.5f, armHeight, 0.5f));
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode upperArmMove = new TransformNode("final movement", m);
        ModelNode upperArmShape = new ModelNode("shape of upperArm", sphere);

        rotateAngle3 = random.nextInt(180) - 90;
        NameNode lampHead = new NameNode("lampHead");
        TransformNode lampHeadUp = new TransformNode("lampHead translates to the top of upperArm", Mat4Transform.translate(0.25f, armHeight, 0));
        lampHeadRotate = new TransformNode("lamp head rotates", Mat4Transform.rotateAroundY(rotateAngle3));
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(headLength, headHeight, headWidth));
        m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode lampHeadMove = new TransformNode("final move", m);
        ModelNode lampHeadShape = new ModelNode("shape of lampHead", cube);

        lamp.addChild(lampTransform);
            lampTransform.addChild(lampUp);
                lampUp.addChild(basement);
                    basement.addChild(basementTransform);
                        basementTransform.addChild(basementShape);
                    basement.addChild(lowerArm);
                        lowerArm.addChild(lowerArmUp);
                            lowerArmUp.addChild(lowerArmRotate);
                                lowerArmRotate.addChild(lowerArmMove);
                                    lowerArmMove.addChild(lowerArmShape);
                                lowerArmRotate.addChild(middleJoint);
                                    middleJoint.addChild(middleJointUp);
                                        middleJointUp.addChild(middleJointMove);
                                            middleJointMove.addChild(middleJointShape);
                                    middleJoint.addChild(upperArm);
                                        upperArm.addChild(upperArmUp);
                                            upperArmUp.addChild(upperArmRotate);
                                                upperArmRotate.addChild(upperArmMove);
                                                    upperArmMove.addChild(upperArmShape);
                                                upperArmRotate.addChild(lampHead);
                                                    lampHead.addChild(lampHeadUp);
                                                        lampHeadUp.addChild(lampHeadRotate);
                                                            lampHeadRotate.addChild(lampHeadMove);
                                                                lampHeadMove.addChild(lampHeadShape);


        lamp.update();
    }

    private void render(GL3 gl){
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
        light.setPosition(getLightPosition());
        light.render(gl);
        board.render(gl);
        floor.setModelMatrix(floor());
        floor.render(gl);
        leg1.render(gl);
        leg2.render(gl);
        leg3.render(gl);
        leg4.render(gl);
        phone.render(gl);
        wall1.setModelMatrix(wall1());
        wall1.render(gl);
        wall2.setModelMatrix(wall2());
        wall2.render(gl);
        lamp.draw(gl);
    }


    private double startTime;

    private double getSeconds(){return System.currentTimeMillis()/1000.0;}

    private Vec3 getLightPosition(){
        double elapsedTime = getSeconds() - startTime;
//        float x = 5.0f * (float) Math.sin(Math.toRadians(elapsedTime*50));
        float x = 0;
        float y = 8.5f;
        float z = 0;
//        float z = 5.0f * (float) Math.cos(Math.toRadians(elapsedTime*50));
        return new Vec3(x,y,z);

    }

    private Mat4 floor() {
        float size = 16f;
        Mat4 modelMatrix = new Mat4(1);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
        return modelMatrix;
    }

    private Mat4 wall1() {
        float size = 16f;
        Mat4 modelMatrix = new Mat4(1);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
        modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
        modelMatrix = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,-size*0.5f), modelMatrix);
        return modelMatrix;
    }

    private Mat4 wall2() {
        float size = 16f;
        Mat4 modelMatrix = new Mat4(1);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
        modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
        modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
        modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,0), modelMatrix);
        return modelMatrix;
    }

}

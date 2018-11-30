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
        this.camera.setPosition(new Vec3(0f, 9f, 16f));
        this.camera.setTarget(new Vec3(0f, 3f, 0f));
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
        worldlight[0].dispose(gl);
        worldlight[1].dispose(gl);
        spotlight.dispose(gl);
        board.dispose(gl);
    }

    public void display(GLAutoDrawable drawable){
        GL3 gl = drawable.getGL().getGL3();
        render(gl);
    }
//
//    public void rotateArm(){
//        Random random = new Random();
//        rotateAngle1 = (random.nextInt(90) - 30) - formerAngle1;
//        rotateAngle2 = (random.nextInt(180) - 90) - formerAngle2;
//        double paseedTime = getSeconds() - startTime;
//        unitAngle1 = rotateAngle1/animationTime;
//        unitAngle2 = rotateAngle2/animationTime;
//        lowerArmRotate.setTransform(Mat4Transform.rotateAroundZ(unitAngle1 * (float)paseedTime + formerAngle1));
//        lowerArmRotate.update();
//        upperArmRotate.setTransform(Mat4Transform.rotateAroundX(unitAngle2 * (float)paseedTime + formerAngle2));
//        upperArmRotate.update();
//        formerAngle1 = rotateAngle1;
//        formerAngle2 = rotateAngle2;
//    }

    public void rotateArm(){
        Random random = new Random();
        rotateAngle1 = (random.nextInt(90) - 30);
        rotateAngle2 = (random.nextInt(180) - 90);
//        double paseedTime = getSeconds() - startTime;
//        unitAngle1 = rotateAngle1/animationTime;
//        unitAngle2 = rotateAngle2/animationTime;
        lowerArmRotate.setTransform(Mat4Transform.rotateAroundZ(rotateAngle1));
        lowerArmRotate.update();
        upperArmRotate.setTransform(Mat4Transform.rotateAroundX(rotateAngle2));
        upperArmRotate.update();
//        formerAngle1 = rotateAngle1;
//        formerAngle2 = rotateAngle2;
    }


//    public float getRotateAngle1(){
//        rotateAngle1 = 0;
//        return rotateAngle1;
//    }

    public void lampJump(){
        Random random = new Random();
        xPosition = random.nextInt(3) - 2;
        zPosition = random.nextInt(3) - 2;
        lampTransform.setTransform(Mat4Transform.translate(xPosition, 0, zPosition));
        lampTransform.update();
    }

    public void shakeHead(){
        Random random = new Random();
        rotateAngle3 = random.nextInt(360);
        lampHeadRotate.setTransform(Mat4Transform.rotateAroundY(rotateAngle3));
        lampHeadRotate.update();
    }

    private Camera camera;
    private Light[] worldlight = new Light[2];
    private Light spotlight;
    private Model floor, wall1, wall2, board, leg1, leg2, leg3, leg4, phone, globebase, globe, rubikcube, cube, cube2, sphere;
    private SGNode lamp;
    private Material materialLight1 = null, materialLight2 = null, materialSpotlight = null;
    private TransformNode lampTransform, lowerArmRotate, upperArmRotate, lampHeadRotate;
    private ModelNode lampHeadShape;
    private float xPosition = 0, zPosition = 0, rotateAngle1 = 0, rotateAngle2 = 0, rotateAngle3 = 0, formerAngle1 = 0, formerAngle2 = 0, formerAngle3 = 0;
    public boolean animation = false;
    private float unitAngle1 = 0, unitAngle2 = 0, animationTime = 1000;

    public void Worldlight1(){
        if (materialLight1 == null){
            materialLight1 = worldlight[0].getMaterial();
        }
        if(worldlight[0].getMaterial() == materialLight1){
            Material materialOff1 = new Material();
            materialOff1.setAmbient(0.1f, 0.1f, 0.1f);
            materialOff1.setDiffuse(0.1f, 0.1f, 0.1f);
            materialOff1.setSpecular(0.1f, 0.1f, 0.1f);
            worldlight[0].setMaterial(materialOff1);
        }
        else {
            worldlight[0].setMaterial(materialLight1);
        }
    }

    public void Worldlight2(){
        if (materialLight2 == null){
            materialLight2 = worldlight[1].getMaterial();
        }
        if(worldlight[1].getMaterial() == materialLight2){
            Material materialOff2 = new Material();
            materialOff2.setAmbient(0.1f, 0.1f, 0.1f);
            materialOff2.setDiffuse(0.1f, 0.1f, 0.1f);
            materialOff2.setSpecular(0.1f, 0.1f, 0.1f);
            worldlight[1].setMaterial(materialOff2);
        }
        else {
            worldlight[1].setMaterial(materialLight2);
        }
    }

    public void Spotlight(){
        if (materialSpotlight == null){
            materialSpotlight = spotlight.getMaterial();
        }
        if(spotlight.getMaterial() == materialSpotlight){
            Material materialOff3 = new Material();
            materialOff3.setAmbient(0f, 0f, 0f);
            materialOff3.setDiffuse(0f, 0f, 0f);
            materialOff3.setSpecular(0f, 0f, 0f);
            spotlight.setMaterial(materialOff3);
        }
        else {
            spotlight.setMaterial(materialSpotlight);
        }
    }

    public void initialise(GL3 gl){

        int[] textureId1 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\board3.jpg");
        int[] textureId2 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\floor.jpg");
        int[] textureId3 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\leg1.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\phone.jpg");
        int[] textureId5 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\wall.jpg");
        int[] textureId6 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\window1.jpg", GL.GL_TEXTURE_MIN_FILTER, GL.GL_TEXTURE_MIN_FILTER,
                                                      GL.GL_LINEAR, GL.GL_LINEAR);
        int[] textureId7 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\base.jpg");
        int[] textureId8 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\arm4.jpg");
        int[] textureId9 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\ear0xuu2.jpg");
        int[] textureId10 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\rubikcube1.jpg");
        int[] textureId11 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\globebase.jpg");
        int[] textureId12 = TextureLibrary.loadTexture(gl, "D:\\jogl\\3D\\src\\mainAssignment\\textures\\head.jpg");

        worldlight[0] = new Light(gl);
        worldlight[0].setCamera(camera);
        worldlight[1] = new Light(gl);
        worldlight[1].setCamera(camera);
        spotlight = new Light(gl);
        spotlight.setCamera(camera);

        float legHeight = 3.5f;
        float boardHeight = legHeight + 3f;
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        Material material = new Material(new Vec3(0.5f, 0.1f, 0.2f), new Vec3(0.5f, 0.1f, 0.2f), new Vec3(0.75f, 0.75f, 0.75f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(10f, 0.5f, 6f), Mat4Transform.translate(0, boardHeight, 0));
        board = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId1);

        float size = 16f;
        mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_tt_05.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_tt_05.txt");
        material = new Material(new Vec3(1, 1, 1), new Vec3(1, 1, 1), new Vec3(0.45f, 0.45f, 0.45f ), 32.0f);
        modelMatrix = Mat4Transform.scale(size, 1f, size);
        floor = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId2);

        mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_tt_05.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_tt_05.txt");
        material = new Material(new Vec3(1, 1, 1), new Vec3(1, 1, 1), new Vec3(0.45f, 0.45f, 0.45f ), 32.0f);
        modelMatrix = Mat4Transform.scale(size, 1f, size);
        wall1 = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId6);

        mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_tt_05.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_tt_05.txt");
        material = new Material(new Vec3(1,1,1), new Vec3(1, 1, 1), new Vec3(0.45f, 0.45f, 0.45f), 32.0f);
        modelMatrix = Mat4Transform.scale(size,1f, size);
        wall2 = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId5);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.6f, 0.6f, 0.6f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(1.0f, legHeight, 1.0f), Mat4Transform.translate(4.5f, 0.5f, 2.5f));
        leg1 = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId3);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.6f, 0.6f, 0.6f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(1.0f, legHeight, 1.0f), Mat4Transform.translate(-4.5f, 0.5f, 2.5f));
        leg2 = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId3);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.6f, 0.6f, 0.6f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(1.0f, legHeight, 1.0f), Mat4Transform.translate(4.5f, 0.5f, -2.5f));
        leg3 = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId3);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.6f, 0.6f, 0.6f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(1.0f, legHeight, 1.0f), Mat4Transform.translate(-4.5f, 0.5f, -2.5f));
        leg4 = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId3);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.75f, 0.5f, 0.2f), new Vec3(0.5f, 0.75f, 0.3f), new Vec3(0.75f, 0.5f, 0.65f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(0.45f, 0.05f, 0.75f), Mat4Transform.translate(-3.5f, 70.5f, 3.0f));
        phone = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId4);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.75f, 0.5f, 0.2f), new Vec3(0.5f, 0.75f, 0.3f), new Vec3(0.75f, 0.5f, 0.65f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(1f, 0.5f, 0.5f), Mat4Transform.translate(2.5f, 7.5f, -3.25f));
        globebase = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId11);

        mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.75f, 0.5f, 0.2f), new Vec3(0.5f, 0.75f, 0.3f), new Vec3(0.75f, 0.5f, 0.65f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(1.5f, 1.5f, 1.5f), Mat4Transform.translate(1.7f, 3.15f, -1.1f));
        globe = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId9);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.75f, 0.5f, 0.2f), new Vec3(0.5f, 0.75f, 0.3f), new Vec3(0.75f, 0.5f, 0.65f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(1f, 1f, 1f), Mat4Transform.translate(3f, 4f, 2f));
        rubikcube = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId10);

        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.8f, 0.75f, 0.6f), new Vec3(0.75f,0.25f, 0.5f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(4f, 4f, 4f), Mat4Transform.translate(0, 0.5f, 0));
        cube = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId7);

        cube2 = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId12);

        mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        shader = new Shader(gl, "D:\\jogl\\3D\\src\\mainAssignment\\vs_cube_04.txt", "D:\\jogl\\3D\\src\\mainAssignment\\fs_cube_04.txt");
        material = new Material(new Vec3(0.75f, 0.5f, 0.2f), new Vec3(0.5f, 0.75f, 0.3f), new Vec3(0.75f, 0.5f, 0.65f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(3f, 3f, 3f), Mat4Transform.translate(0, 0.5f, 0));
        sphere = new Model(gl, camera, worldlight, spotlight, shader, material, modelMatrix, mesh, textureId8);


        float basementHeight = 0.5f;
        float armHeight = 1.5f;
        float jointDia = 0.5f;
        float headLength = 1.0f;
        float headWidth = 0.5f;
        float headHeight = 0.5f;

        lamp = new NameNode("lamp");

        lampTransform = new TransformNode("lamp rotates", Mat4Transform.translate(xPosition, 0, zPosition));

        TransformNode lampUp = new TransformNode("lamp translate to the top of table", Mat4Transform.translate(0, 3.5f, 0));

        NameNode basement = new NameNode("basement");
         Mat4 m = Mat4Transform.scale(2.5f, basementHeight, 0.75f);
         m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f,0));
         TransformNode basementTransform = new TransformNode("basement transform", m);
         ModelNode basementShape = new ModelNode("cube(basement)", cube);

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

        NameNode upperArm = new NameNode("upperArm");
         TransformNode upperArmUp = new TransformNode("upperArm translates to the top of middleJoint", Mat4Transform.translate(0, armHeight+jointDia, 0));
         upperArmRotate = new TransformNode("upper arm rotates", Mat4Transform.rotateAroundZ(rotateAngle2));
         m = new Mat4(1);
         m = Mat4.multiply(m, Mat4Transform.scale(0.5f, armHeight, 0.5f));
         m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
         TransformNode upperArmMove = new TransformNode("final movement", m);
         ModelNode upperArmShape = new ModelNode("shape of upperArm", sphere);

        NameNode lampHead = new NameNode("lampHead");
         TransformNode lampHeadUp = new TransformNode("lampHead translates to the top of upperArm", Mat4Transform.translate(0.25f, armHeight, 0));
         lampHeadRotate = new TransformNode("lamp head rotates", Mat4Transform.rotateAroundY(rotateAngle3));
         m = new Mat4(1);
         m = Mat4.multiply(m, Mat4Transform.scale(headLength, headHeight, headWidth));
         m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
         TransformNode lampHeadMove = new TransformNode("final move", m);
         lampHeadShape = new ModelNode("shape of lampHead", cube2);

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
        worldlight[0].setPosition(getLightPosition1());
        worldlight[0].render(gl);
        worldlight[1].setPosition(getLightPosition2());
        worldlight[1].render(gl);
        spotlight.setPosition(getLightPosition3());
        spotlight.setDirection(getSpotLightDirection());
        spotlight.render(gl);
        board.render(gl);
        floor.setModelMatrix(floor());
        floor.render(gl);
        leg1.render(gl);
        leg2.render(gl);
        leg3.render(gl);
        leg4.render(gl);
        phone.render(gl);
        globebase.render(gl);
        globe.render(gl);
        rubikcube.render(gl);
        wall1.setModelMatrix(wall1());
        wall1.render(gl);
        wall2.setModelMatrix(wall2());
        wall2.render(gl);
        if (animation) rotateArm();
        lamp.draw(gl);
    }


    private Vec3 getLightPosition1(){
        float x = 3f;
        float y = 9f;
        float z = 3f;
        return new Vec3(x,y,z);

    }

    private Vec3 getLightPosition2(){
        float x = -3f;
        float y = 9.5f;
        float z = -3f;
        return new Vec3(x,y,z);
    }

    private Vec3 getLightPosition3(){
        Mat4 headTransform = Mat4.multiply(lampHeadShape.worldTransform, Mat4Transform.translate(0.6f, 0, 0));
        Vec3 lightPosition = new Vec3();
        float [] position = headTransform.getLightVertex();
        lightPosition.x = position[0];
        lightPosition.y = position[1];
        lightPosition.z = position[2];
        return lightPosition;
    }

    private Vec3 getSpotLightDirection(){
        Vec3 direction = new Vec3();
        float [] spotlightdirection = lampHeadShape.getWorldTransform().getLightDirection();
        direction.x = spotlightdirection[0];
        direction.y = spotlightdirection[1];
        direction.z = spotlightdirection[2];
        return direction;
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

    private double startTime = 0;
    private double getSeconds(){return System.currentTimeMillis()/1000.0; }
}

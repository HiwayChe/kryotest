package com.hiway.kryo.test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.*;

/**
 * This class is to test kryo serialize and deserialize when JavaBean field type changed.
 * in Kryo 4, deserialize succeed but data is wrong, in Kryo 5, deserialize also succeed and crash jvm if you have done operation on wrong data
 * @date 2019/3/25 0025
 */
public class Main {

    public static void main(String... args) throws FileNotFoundException {
        File file = new File("D:/person.ser");
        Main main = new Main();

        // Kryo 4 test
//        main.testKryo4(file);

        //Kryo 5.0 test
        main.testKryo5(file);
    }

    /**
     * test Kryo 4.0.2, do as comment instruction
     * @param file
     * @throws FileNotFoundException
     */
    private void testKryo4(File file) throws FileNotFoundException {
        //serialize, Person.id is String type
//        Person person = new Person();
//        person.setId("hacker");
//        FileOutputStream fileOutputStream = new FileOutputStream(file);
//        this.serialize(person, fileOutputStream);

        //deserialize
        //1. comment above code
        //2. change Person.id to Long
        //3. uncomment below code and execute
        Person p = (Person) this.deserialize(new FileInputStream(file));
        System.out.println(p.getId() + 1L);//print -48

    }

    /**
     * test Kryo 5.0 RC1, do as comment instruction
     * @param file
     * @throws FileNotFoundException
     */
    private void testKryo5(File file) throws FileNotFoundException {
        //serialize, Person.id is String type
        Person person = new Person();
        person.setId("hacker");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        this.serialize(person, fileOutputStream);

        //deserialize
        //1. uncomment above code
        //2. change Person.id to Long
        //3. uncomment below code and execute
//        Person deserialize = (Person) this.deserialize(new FileInputStream(file));
//        System.out.println(deserialize.getId() + 1L);//jvm crash
    }


    private Kryo getKryo() {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy())); //Kryo 5.0.0-RC1
//        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy())); //Kryo 4.0.2
        kryo.setReferences(false);
        kryo.setRegistrationRequired(false);
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        return kryo;
    }

    private void serialize(Object object, OutputStream outputStream) throws FileNotFoundException {
        Kryo kryo = this.getKryo();
        Output output = new Output(1024, 102400);
        output.setOutputStream(outputStream);
        kryo.writeClassAndObject(output, object);
        output.close();
    }

    private Object deserialize(InputStream inputStream) {
        Kryo kryo = this.getKryo();
        Input input = new Input(102400);
        input.setInputStream(inputStream);
        Object o = kryo.readClassAndObject(input);
        input.close();
        return o;
    }
}

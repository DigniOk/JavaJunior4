import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class Main {

    public static void main(String[] args) {
        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            create(sessionFactory);
            System.out.println(search(sessionFactory));
            update(sessionFactory);
            delete(sessionFactory);
            simpleQuery(sessionFactory);
        }
    }

    private static void create(SessionFactory sessionFactory) {
        Student student = new Student();
          student.setId(2L);
          student.setFirstName("Diana");
          student.setLastName("Harlamova");
          student.setAge(20);
        Student student1 = new Student();
          student1.setId(1L);
          student1.setFirstName("Nick");
          student1.setLastName("Krapivin");
          student1.setAge(21);
        Student student2 = new Student();
          student2.setId(3L);
          student2.setFirstName("Anton");
          student2.setLastName("Berezkin");
          student2.setAge(22);

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(student);
            session.persist(student1);
            session.persist(student2);
           tx.commit();
        }
    }

    private static Student search(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Student.class, 1L);
        }
    }

    private static void update(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Student student = session.find(Student.class, 2L);
            student.setLastName("Hibernate");
            session.merge(student);
            tx.commit();
        }
    }

    private static void delete(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Student student = search(sessionFactory);
            Transaction tx = session.beginTransaction();
            session.remove(student);
            tx.commit();
        }
    }

    private static void simpleQuery(SessionFactory sessionFactory) {
        try(Session session = sessionFactory.openSession()) {
            Query<Student> query = session.createQuery("select s from Student s where s.age <= :age", Student.class);
            query.setParameter("age", 21);
            System.out.println(query.getSingleResult());
        }
    }
}

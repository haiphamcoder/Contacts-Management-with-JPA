import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class ContactManager {
    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    public ContactManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void close() {
        entityManager.close();
        entityManagerFactory.close();
    }

    public void addContact(Contact contact) {
        entityManager.getTransaction().begin();
        entityManager.persist(contact);
        entityManager.getTransaction().commit();
    }

    public void updateContact(Contact contact) {
        entityManager.getTransaction().begin();
        entityManager.merge(contact);
        entityManager.getTransaction().commit();
    }

    public void deleteContact(Contact contact) {
        entityManager.getTransaction().begin();
        if (contact != null) {
            contact = entityManager.merge(contact);
        }
        entityManager.getTransaction().commit();
    }

    public Contact getContact(Long id) {
        return entityManager.find(Contact.class, id);
    }

    public List<Contact> getAllContacts() {
        return entityManager.createQuery("SELECT c FROM Contact c", Contact.class).getResultList();
    }

    public static void main(String[] args) {
        ContactManager contactManager = new ContactManager();

        // Get all contacts
        List<Contact> lstContacts = contactManager.getAllContacts();
        System.out.println("All contacts:");
        for (Contact contact : lstContacts) {
            System.out.println(contact);
        }
        System.out.println();

        // Add a new contact
        Contact contact = new Contact(
                "Nguyen Van G",
                "nguyenvang@gmail.com",
                "0123456789"
        );
        contactManager.addContact(contact);
        lstContacts = contactManager.getAllContacts();
        System.out.println("All contacts after adding a new one:");
        for (Contact c : lstContacts) {
            System.out.println(c);
        }
        System.out.println();

        // Remove a contact
        contact = contactManager.getContact(7L);
        contactManager.deleteContact(contact);
        lstContacts = contactManager.getAllContacts();
        System.out.println("All contacts after removing a contact:");
        for (Contact c : lstContacts) {
            System.out.println(c);
        }
        System.out.println();

        // Update a contact
        contact = contactManager.getContact(1L);
        contact.setName("Nguyen Van Mot");
        contactManager.updateContact(contact);
        lstContacts = contactManager.getAllContacts();
        System.out.println("All contacts after updating a contact:");
        for (Contact c : lstContacts) {
            System.out.println(c);
        }
        System.out.println();

        contactManager.close();
    }
}

package connection;
import presentationLayer.MainFrame;

import javax.swing.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataAccessClass<T> {
    private final Class<T> type;
    private ConnectionFactory connectionFactory;
    private MainFrame mainFrame;


    public DataAccessClass(ConnectionFactory connectionFactory, T instance)
    {
        this.type=(Class<T>) (instance.getClass());
        this.connectionFactory= connectionFactory;
    }

    public DataAccessClass(ConnectionFactory connectionFactory, T instance, MainFrame mainFrame)
    {
        this.type=(Class<T>) (instance.getClass());
        this.connectionFactory= connectionFactory;
        this.mainFrame = mainFrame;
    }

    /**
     * Reads all the entries from the database table which corresponds to the class T.
     * @return a list of created objects of type T.
     */
    public ArrayList<T> findAll()
    {
        ResultSet rs = null;
        Statement st=null;
        try {
            st = connectionFactory.getConnection().createStatement();
            rs = st.executeQuery("SELECT * from " + connectionFactory.getDATABASENAME() + "." + type.getSimpleName());
            return createObjects(rs);
        }
        catch (SQLException e)
        {
            System.out.println("ERROR trying to findAll\n");
            e.printStackTrace();
            return null;
        }
        finally {
            try{ if(rs!=null) rs.close(); if(st!=null) st.close();}
            catch(SQLException e){ System.out.println("ERROR closing\n"); e.printStackTrace();}
        }
    }

    /**
     * updates an entry in the database table which corresponds to the class T.
     * @param instance the object whose data will be written in the table.
     */
    public void update(T instance)
    {
        Statement st=null;
        try {
            st = connectionFactory.getConnection().createStatement();
            String statement = new String("update " + connectionFactory.getDATABASENAME() + "." + type.getSimpleName()+" set ");
            String where = " WHERE ";
            boolean first=true;
            for(Field field: type.getDeclaredFields()) {
                try {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(),type);
                    Method method = propertyDescriptor.getReadMethod();
                    Object value;
                    value = method.invoke(instance);
                    if(first)
                    {
                        where = where + field.getName() + " = '" + String.valueOf(value) +"';";
                        first=false;
                    } else
                        statement = statement + field.getName() + " = '" + String.valueOf(value) + "', ";

                } catch (Exception e) { System.out.println("ERROR updating\n"); e.printStackTrace();}
            }
            statement= statement.substring(0, statement.length()-2);
            statement= statement + where;
            st.executeUpdate(statement);
        }
        catch (SQLException e) { System.out.println("SQL problem when trying to update\n"); }
        finally {
            try{ if(st!=null) st.close();}
            catch(SQLException e){ System.out.println("ERROR closing\n"); e.printStackTrace();}
        }
    }

    /**
     * adds an entry in the database table which corresponds to the class T.
     * @param instance the object whose data will be written in the table.
     */
    public void insert(T instance)
    {
        Statement st=null;
        try {
            st = connectionFactory.getConnection().createStatement();
            String statement = new String("INSERT INTO " + connectionFactory.getDATABASENAME() + "." + type.getSimpleName()+" (");
            String values = " VALUES (";
            for(Field field: type.getDeclaredFields()) {
                try {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(),type);
                    Method method = propertyDescriptor.getReadMethod();
                    Object value;
                    value = method.invoke(instance);

                    statement = statement + "`"+field.getName()+"`, ";
                    values = values + "'"+String.valueOf(value)+"', ";

                } catch (Exception e) { System.out.println("ERROR adding\n"); e.printStackTrace();}
            }
            statement= statement.substring(0, statement.length()-2);
            statement= statement + ")";
            values=values.substring(0, values.length()-2);
            values=values+");";
            statement = statement + values;
            st.executeUpdate(statement);
        }
        catch (SQLException e) { System.out.println("SQL problem when trying to insert\n"); }
        finally {
            try{ if(st!=null) st.close();}
            catch(SQLException e){ System.out.println("ERROR closing\n"); e.printStackTrace();}
        }
    }


    /**
     * deletes an entry in the database table which corresponds to the class T.
     * @param id the id of the entry that will be deleted.
     */
    public int deleteByID(int id)
    {
        Statement st=null;
        try
        {
            st = connectionFactory.getConnection().createStatement();
            String statement = new String("DELETE FROM " + connectionFactory.getDATABASENAME() + "." + type.getSimpleName()+" WHERE id="+id+";");
            st.executeUpdate(statement);
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Cannot delete because of foreign key. Information will persist.");
            System.out.println("SQL problem when trying to delete by ID\n");
            return -2;
        }
        finally {
            try{ if(st!=null) st.close();}
            catch(SQLException e){ System.out.println("ERROR closing\n"); e.printStackTrace();}
        }
        return 0;
    }


    public T findByID(int id)
    {
        ResultSet rs = null;
        Statement st=null;
        try
        {
            st = connectionFactory.getConnection().createStatement();
            String statement = new String("SELECT * FROM " + connectionFactory.getDATABASENAME() + "." + type.getSimpleName()+" WHERE id="+id+";");
            rs=st.executeQuery(statement);
            return createObjects(rs).get(0);
        }
        catch (SQLException e)
        {
            System.out.println("SQL problem when trying to find by ID\n");
            return null;
        }
        finally {
            try{ if(rs!=null) rs.close(); if(st!=null) st.close();}
            catch(SQLException e){ System.out.println("ERROR closing\n"); e.printStackTrace();}
        }
    }

    private ArrayList<T> createObjects(ResultSet rs)
    {
        ArrayList<T> list= new ArrayList<>();
        try {
            while(rs.next()) {
                T instance = type.getDeclaredConstructor().newInstance();
                //T instance = type.newInstance();
                for(Field field: type.getDeclaredFields()) {
                    Object value= rs.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(),type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        }
        catch (Exception e) {
            System.out.println("error while creating objects\n");
            e.printStackTrace();
        }
        finally { return list; }
    }
}

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CBTLPR_TP_04 
{
	//Desenvolvido por Beatriz Bastos Borges e Miguel Luizatto Alves
	
    private static ResultSet resultSet; 
    private static Connection connection;

    public static void main(String args[]) 
    {
        startFrame();
    }

    public static void startFrame() 
    {
        JFrame f = new JFrame("Trabalho Pratico 04");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 250);
        f.setLayout(null);

        JLabel lblPesquisa = new JLabel("Nome:");
        lblPesquisa.setBounds(10, 10, 50, 25);
        f.add(lblPesquisa);

        JTextField txtPesquisa = new JTextField();
        txtPesquisa.setBounds(60, 10, 200, 25);
        f.add(txtPesquisa);

        JButton btnPesquisa = new JButton("Pesquisar");
        btnPesquisa.setBounds(270, 10, 100, 25);
        f.add(btnPesquisa);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(10, 50, 50, 25);
        f.add(lblNome);

        JTextField txtNome = new JTextField();
        txtNome.setBounds(80, 50, 290, 25);
        txtNome.setEditable(false);
        f.add(txtNome);

        JLabel lblSalario = new JLabel("Salário:");
        lblSalario.setBounds(10, 90, 50, 25);
        f.add(lblSalario);

        JTextField txtSalario = new JTextField();
        txtSalario.setBounds(80, 90, 290, 25);
        txtSalario.setEditable(false);
        f.add(txtSalario);

        JLabel lblCargo = new JLabel("Cargo:");
        lblCargo.setBounds(10, 130, 50, 25);
        f.add(lblCargo);

        JTextField txtCargo = new JTextField();
        txtCargo.setBounds(80, 130, 290, 25);
        txtCargo.setEditable(false);
        f.add(txtCargo);

        JButton btnAnterior = new JButton("Anterior");
        btnAnterior.setBounds(80, 170, 100, 25);
        btnAnterior.setEnabled(false); 
        f.add(btnAnterior);

        JButton btnProximo = new JButton("Próximo");
        btnProximo.setBounds(200, 170, 100, 25);
        btnProximo.setEnabled(false);
        f.add(btnProximo);

        btnPesquisa.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String nomePesquisa = txtPesquisa.getText();
                try 
                {
                    connection = Database.getConnection();
                    String query = "SELECT nome_func, sal_func, cod_cargo FROM tbfuncs WHERE nome_func LIKE ?";
                    PreparedStatement stmt = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    stmt.setString(1, "%" + nomePesquisa + "%");
                    resultSet = stmt.executeQuery();

                    if (resultSet.next()) 
                    {
                        btnAnterior.setEnabled(true);
                        btnProximo.setEnabled(true);
                        updateFields(resultSet, txtNome, txtSalario, txtCargo);
                    } 
                    else 
                    {
                        JOptionPane.showMessageDialog(f, "Nenhum resultado encontrado.");
                        btnAnterior.setEnabled(false);
                        btnProximo.setEnabled(false);
                    }
                } 
                catch (SQLException ex) 
                {
                    ex.printStackTrace();
                }
            }
        });

        // Ação do botão "Anterior"
        btnAnterior.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try 
                {
                    if (resultSet.previous()) 
                    {
                        updateFields(resultSet, txtNome, txtSalario, txtCargo);
                    } 
                    else 
                    {
                        JOptionPane.showMessageDialog(f, "Você já está no primeiro registro.");
                    }
                } 
                catch (SQLException ex) 
                {
                    ex.printStackTrace();
                }
            }
        });

        // Ação do botão "Próximo"
        btnProximo.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try 
                {
                    if (resultSet.next()) 
                    {
                        updateFields(resultSet, txtNome, txtSalario, txtCargo);
                    } 
                    else 
                    {
                        JOptionPane.showMessageDialog(f, "Você já está no último registro.");
                    }
                } 
                catch (SQLException ex) 
                {
                    ex.printStackTrace();
                }
            }
        });

        f.setVisible(true);
    }

    private static void updateFields(ResultSet rs, JTextField txtNome, JTextField txtSalario, JTextField txtCargo) throws SQLException 
    {
        txtNome.setText(rs.getString("nome_func"));
        txtSalario.setText(String.valueOf(rs.getDouble("sal_func")));
        txtCargo.setText(String.valueOf(rs.getInt("cod_cargo")));
    }
}

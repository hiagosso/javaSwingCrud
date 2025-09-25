import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class App {

    public static void main(String[] args) {
        DataBase db = new DataBase();
        db.creaTable();

        JFrame frame = new JFrame("CRUD");
        frame.setSize(500, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel textNome = new JLabel("Nome");

        JTextField campoNome = new JTextField();
        campoNome.setPreferredSize(new Dimension(100, 30));

        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int colum){
                return false;
            }
        };
        String[] colunas = { "id", "Nome" };
        for (String coluna : colunas) {
            model.addColumn(coluna);
        }

        JTable tabela = new JTable(model);
        tabela.getTableHeader().setResizingAllowed(false);
        tabela.getTableHeader().setReorderingAllowed(false);
        JScrollPane jsp = new JScrollPane(tabela);

        JButton btnInserir = new JButton("Inserir");
        btnInserir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.insertTable(campoNome.getText());
                aviso(frame,campoNome.getText() + " Inserido!",1500);
                campoNome.setText("");
                carregarUsuarios(model, db);
            }
        });

        JButton btnDeletar = new JButton("Deletar");
        btnDeletar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String idStr = (String) tabela.getValueAt(tabela.getSelectedRow(), 0);
                    int idSelect = Integer.parseInt(idStr);
                    db.deleteById(idSelect);
                    aviso(frame,"ID " + idSelect + " Deletado!",1500);
                } catch (RuntimeException ex) {
                    aviso(frame,"Selecione um ID valido!",1500);
                }
                carregarUsuarios(model, db);
            }
        });

        JButton btnUpdate = new JButton("Atualizar");
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String idStr = (String) tabela.getValueAt(tabela.getSelectedRow(), 0);
                    int idSelect = Integer.parseInt(idStr);
                    String nome = campoNome.getText();
                    if(campoNome.getText().equals("")){
                        aviso(frame,"Campo vazio",1500);
                        return;
                    }
                    db.updateById(idSelect,nome);
                    aviso(frame,"ID " + idSelect + " Atualizado!",1500);
                } catch (RuntimeException ex) {
                    aviso(frame,"Selecione um ID valido!",1500);
                }
                carregarUsuarios(model, db);
            }
        });

        carregarUsuarios(model, db);



        frame.add(panel, BorderLayout.NORTH);
        panel.add(textNome);
        panel.add(campoNome);
        panel.add(btnInserir);
        frame.add(jsp, BorderLayout.CENTER);

        JPanel grupoBtns = new JPanel();
        grupoBtns.add(btnUpdate);
        grupoBtns.add(btnDeletar);

        frame.add(grupoBtns, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public static void carregarUsuarios(DefaultTableModel model, DataBase db) {
        model.setRowCount(0);
        for (String[] linha : db.readTable()) {
            model.addRow(linha);
        }
    }

    public static void aviso(JFrame parent, String mensagem, int milissegundos) {
        JOptionPane pane = new JOptionPane(mensagem, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(parent, "Aviso");

        Timer timer = new Timer(milissegundos, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
    }

}

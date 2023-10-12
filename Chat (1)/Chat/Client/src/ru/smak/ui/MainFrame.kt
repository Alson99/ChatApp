
import ru.smak.ui.UI
import java.awt.Dimension
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.GroupLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField

class MainFrame : JFrame(), UI {

    private val minSize = Dimension(600, 450)
    private val lblHint = JLabel("Enter your name:")
    private val tfMessage = JTextField()
    private val btnSend = JButton("Send")
    private val thisChat = JTextArea()
    private val spScroller = JScrollPane(thisChat)
    init{
        defaultCloseOperation = EXIT_ON_CLOSE
        minimumSize = minSize
        size = minSize
        thisChat.isEditable = false
        btnSend.addActionListener {
            receiver?.invoke(tfMessage.text)
            tfMessage.text = ""
        }
        tfMessage.addKeyListener(object : KeyAdapter(){
            override fun keyTyped(e: KeyEvent?) {
                if (e?.keyChar == '\n')
                    btnSend.doClick()
            }
        })
        layout = GroupLayout(contentPane).apply {
            setVerticalGroup(
                createSequentialGroup()
                    .addGap(8)
                    .addComponent(lblHint, MIN_SZ, MIN_SZ, MIN_SZ)
                    .addGap(8)
                    .addGroup(
                        createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(tfMessage, 27, MIN_SZ, MIN_SZ)
                            .addComponent(btnSend, 27, MIN_SZ, MIN_SZ)
                    )
                    .addGap(8)
                    .addComponent(spScroller, MAX_SZ, MAX_SZ, MAX_SZ)
                    .addGap(8)
            )
            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(8)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(lblHint, MAX_SZ, MAX_SZ, MAX_SZ)
                            .addGroup(
                                createSequentialGroup()
                                    .addComponent(tfMessage, MAX_SZ, MAX_SZ, MAX_SZ)
                                    .addGap(8)
                                    .addComponent(btnSend, MIN_SZ, MIN_SZ, MIN_SZ)
                            )
                            .addComponent(spScroller, MAX_SZ, MAX_SZ, MAX_SZ)
                    )
                    .addGap(8)
            )
        }
    }

    companion object{
        const val MIN_SZ = GroupLayout.PREFERRED_SIZE
        const val MAX_SZ = GroupLayout.DEFAULT_SIZE
    }

    override fun showMessage(msg: String) {
        thisChat.text += msg + '\n'
    }

    override fun showComment(cmt: String) {
        thisChat.text += cmt + '\n'
    }

    override var receiver: ((String) -> Unit)? = null
}
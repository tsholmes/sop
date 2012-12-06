package sop.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import sop.SopInterpreter;
import sop.SopParser;
import sop.inst.SopInstruction;

public class SopFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;

	private JTextArea codeTextArea;

	private JButton parseButton;
	private JButton stopButton;
	private JButton pauseButton;
	private JButton playButton;
	private JButton stepButton;

	private JTable instructionsTable;
	
	private JTable stackTable;
	
	private JTextArea outputTextArea;

	private JLabel statusLabel;

	private boolean parsed = false;
	private boolean running = false;

	private SopInstruction[] instructions = null;

	private SopInterpreter interpreter = null;
	
	private ByteArrayOutputStream output = new ByteArrayOutputStream();

	public SopFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			System.err
					.println("Could not find system look and feel. Using default.");
		}

		{
			JMenuBar menuBar = new JMenuBar();
			{
				JMenu fileMenu = new JMenu("File");
				{
					openMenuItem = new JMenuItem("Open");
					openMenuItem.addActionListener(this);
					fileMenu.add(openMenuItem);

					saveMenuItem = new JMenuItem("Save");
					saveMenuItem.addActionListener(this);
					fileMenu.add(saveMenuItem);

					saveAsMenuItem = new JMenuItem("Save As");
					saveAsMenuItem.addActionListener(this);
					fileMenu.add(saveAsMenuItem);
				}
				menuBar.add(fileMenu);
			}
			setJMenuBar(menuBar);
		}
		{
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			{
				JPanel topPanel = new JPanel();
				topPanel.setLayout(new BorderLayout());
				{
					JLabel codeLabel = new JLabel("Code");
					topPanel.add(codeLabel, BorderLayout.NORTH);

					codeTextArea = new JTextArea();
					codeTextArea.requestFocusInWindow();

					JScrollPane codeScrollPane = new JScrollPane(codeTextArea);
					codeScrollPane.setPreferredSize(new Dimension(500, 200));
					codeScrollPane.setMinimumSize(new Dimension(400, 50));

					topPanel.add(codeScrollPane, BorderLayout.CENTER);
				}
				JPanel bottomPanel = new JPanel();
				bottomPanel.setLayout(new GridLayout(0, 1));
				{
					JPanel controlButtonPanel = new JPanel();
					controlButtonPanel.setMaximumSize(new Dimension(2000, 30));
					{
						parseButton = new JButton("Parse Program");
						parseButton.addActionListener(this);
						controlButtonPanel.add(parseButton);

						stopButton = new JButton("Stop");
						stopButton.addActionListener(this);
						controlButtonPanel.add(stopButton);

						pauseButton = new JButton("Pause");
						pauseButton.addActionListener(this);
						controlButtonPanel.add(pauseButton);

						playButton = new JButton("Play");
						playButton.addActionListener(this);
						controlButtonPanel.add(playButton);

						stepButton = new JButton("Step");
						stepButton.addActionListener(this);
						controlButtonPanel.add(stepButton);
					}
					bottomPanel.add(controlButtonPanel);

					JPanel instructionsPanel = new JPanel();
					instructionsPanel.setLayout(new BorderLayout());
					{
						JLabel instructionsLabel = new JLabel("Instructions");
						instructionsPanel.add(instructionsLabel,
								BorderLayout.NORTH);

						instructionsTable = new JTable();
						JScrollPane instructionsScrollPane = new JScrollPane(
								instructionsTable);
						instructionsScrollPane.setPreferredSize(new Dimension(
								500, 25));
						instructionsScrollPane.setMinimumSize(new Dimension(
								400, 25));
						instructionsPanel.add(instructionsScrollPane,
								BorderLayout.CENTER);
					}
					bottomPanel.add(instructionsPanel);

					JPanel stackPanel = new JPanel();
					stackPanel.setLayout(new BorderLayout());
					{
						JLabel stackLabel = new JLabel("Stack");
						stackPanel.add(stackLabel,
								BorderLayout.NORTH);

						stackTable = new JTable();
						JScrollPane stackScrollPane = new JScrollPane(
								stackTable);
						stackScrollPane.setPreferredSize(new Dimension(
								500, 25));
						stackScrollPane.setMinimumSize(new Dimension(
								400, 25));
						stackPanel.add(stackScrollPane,
								BorderLayout.CENTER);
					}
					bottomPanel.add(stackPanel);

					JPanel outputPanel = new JPanel();
					outputPanel.setLayout(new BorderLayout());
					{
						JLabel outputLabel = new JLabel("Output");
						outputPanel.add(outputLabel,
								BorderLayout.NORTH);

						outputTextArea = new JTextArea();
						outputTextArea.setEditable(false);
						JScrollPane outputScrollPane = new JScrollPane(
								outputTextArea);
						outputScrollPane.setPreferredSize(new Dimension(
								500, 25));
						outputScrollPane.setMinimumSize(new Dimension(
								400, 25));
						outputPanel.add(outputScrollPane,
								BorderLayout.CENTER);
					}
					bottomPanel.add(outputPanel);
				}
				JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
						topPanel, bottomPanel);
				panel.add(split, BorderLayout.CENTER);

				statusLabel = new JLabel(" ");
				panel.add(statusLabel, BorderLayout.SOUTH);
			}
			panel.setMinimumSize(new Dimension(480,200));
			panel.setPreferredSize(new Dimension(800, 600));
			setContentPane(panel);
		}

		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setMinimumSize(new Dimension(480,300));

		setButtons();
		loadInstructions();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();

		if (openMenuItem == source) {

		} else if (saveMenuItem == source) {

		} else if (saveAsMenuItem == source) {

		} else if (parseButton == source) {
			parseAndLoad();
		} else if (stopButton == source) {
			parsed = running = false;
			instructions = null;
			interpreter = null;
			loadInstructions();
			loadStack();
		} else if (pauseButton == source) {

		} else if (playButton == source) {

		} else if (stepButton == source) {
			interpreter.executeInstruction();
			loadStack();
			setCurrentInstruction();
		}
		setButtons();
		setOutput();
	}

	private void parseAndLoad() {
		String text = codeTextArea.getText();
		SopParser parser = new SopParser(text);
		try {
			instructions = parser.parse();
			output = new ByteArrayOutputStream();
			interpreter = new SopInterpreter(instructions, System.in,
					output);
			statusLabel.setText(instructions.length + " instructions parsed");
			parsed = true;
		} catch (Exception ex) {
			output = new ByteArrayOutputStream();
			statusLabel.setText(ex.getMessage());
			parsed = false;
			instructions = null;
		}
		loadInstructions();
		loadStack();
	}

	private void setButtons() {
		parseButton.setEnabled(true);
		stopButton.setEnabled(parsed);
		pauseButton.setEnabled(parsed && running);
		playButton.setEnabled(parsed && !running);
		stepButton.setEnabled(parsed && !running);
	}

	private void loadInstructions() {
		if (instructions == null) {
			instructionsTable.setModel(new DefaultTableModel(new Object[0][],
					new Object[0]));
		} else {
			Object[] headers = new Object[instructions.length];
			for (int i = 0; i < headers.length; i++) {
				headers[i] = i + "";
			}
			instructionsTable.setModel(new DefaultTableModel(
					new Object[][] { instructions, new Object[instructions.length] }, headers));
			setCurrentInstruction();
		}
		instructionsTable.setEnabled(false);
		instructionsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
	
	private void setCurrentInstruction() {
		int ex = interpreter.getExPos();
		for (int i = 0; i < instructions.length; i++) {
			instructionsTable.setValueAt((i==ex)?"*":"", 1, i);
		}
		instructionsTable.scrollRectToVisible(instructionsTable.getCellRect(0, ex, true));
	}
	
	private void loadStack() {
		if (interpreter == null) {
			stackTable.setModel(new DefaultTableModel(new Object[0][],
					new Object[0]));
		} else {
			int[] stack = interpreter.getStack();
			Object[] headers = new Object[stack.length];
			Object[] values = new Object[stack.length];
			for (int i = 0; i < headers.length; i++) {
				headers[i] = i + "";
				values[i] = stack[i] + "";
			}
			stackTable.setModel(new DefaultTableModel(
					new Object[][] { values }, headers));
		}
		stackTable.setEnabled(false);
		stackTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
	
	private void setOutput() {
		ByteArrayInputStream outputRead = new ByteArrayInputStream(output.toByteArray());
		Scanner in = new Scanner(outputRead);
		String text = "";
		while (in.hasNextLine()) {
			text += in.nextLine() + "\n";
		}
		outputTextArea.setText(text);
	}
}

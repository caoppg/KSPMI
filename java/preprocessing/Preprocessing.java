package preprocessing;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.RemoveUseless;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.attribute.NumericToBinary;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;
import weka.attributeSelection.Ranker;
import weka.attributeSelection.InfoGainAttributeEval;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import weka.attributeSelection.AttributeSelection;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.NominalToString;

public class Preprocessing {
	
	/**
	 * Number of attributes to keep
	 */
	private static final int NATTRIBUTES_TO_KEEP = 10;
	
	/**
	 * Hash map between labels (discretization intervals) and their correspondent integer (SPMF)
	 */
	private HashMap<String, Integer> _labels2int = null;
	
	/**
	 * List with entry i being the label of i (converse of previous map)
	 */
	private ArrayList<String> _int2labels = null;
	
	/**
	 * Hash map between attributes' names and their correspondent integers (SPMF)
	 * coding for the attribute's values.
	 */
	private HashMap<String, ArrayList<Integer>> _labels2bins = null;
	
	/**
	 * Données.
	 */
	private Instances _data;

	/**
	 * Cette fonction permet d'étiqueter les valeurs des attributs discrétisés.
	 */
	public void labelize() {
		_int2labels = new ArrayList<String>();
		_labels2int = new HashMap<String, Integer>();
		_labels2bins = new HashMap<String, ArrayList<Integer>>();
		for (int i = 0, s = 1; i < _data.numAttributes() - 2; ++i) {
			ArrayList<Integer> labels_in_bin = new ArrayList<Integer>();
			_labels2bins.put(_data.attribute(i).name(), labels_in_bin);
			for (Enumeration<Object> e = _data.attribute(i).enumerateValues(); e.hasMoreElements(); ++s) {
				String label = (String) e.nextElement();
				_int2labels.add(label);
				_labels2int.put(label, Integer.valueOf(s));
				labels_in_bin.add(s);
			}
		}

		for (int i = 0, s = 1; i < _data.numAttributes() - 2; ++i) {
			Attribute att = _data.attribute(i);
			for (int j = 0; j < att.numValues(); ++j, ++s) {
				_data.renameAttributeValue(att, att.value(j), String.valueOf(s));
			}
		}
	}
	
	/**
	 * Cette méthode permet pré-traiter les horodatages. Les timestamps sont convertis en chaîne de
	 * caractères pour après ce voir concaténer des chevrons (<>) au début et à la fin. Ceci est 
	 * essentiel pour que le lecteur de SPMF tienne bien compte de ces valeurs en tant que horodatage.
	 * @throws Exception Liée aux transformations de l'attribut
	 */
	public void prepareTimestamps() throws Exception {
		// On est obligés de passé par la nominalisation
		NumericToNominal ntn = new NumericToNominal();
		ntn.setAttributeIndices(String.valueOf(_data.numAttributes() - 1));
		ntn.setInputFormat(_data);
		_data = Filter.useFilter(_data, ntn);
		
		// On transforme les valeurs nominales en chaînes de caractères
		NominalToString nts = new NominalToString();
		nts.setAttributeIndexes(String.valueOf(_data.numAttributes() - 1));
		nts.setInputFormat(_data);
		_data = Filter.useFilter(_data, nts);
		
		// Finalement, on ajoute des chevrons au début et à la fin de chaque horodatage
		Attribute timestamp = _data.attribute(_data.numAttributes() - 2);
		for (int i = 0; i < _data.size(); ++i) {
			Instance ins = _data.get(i);
			String newVal = "<" + ins.stringValue(timestamp) + ">";
			timestamp.addStringValue(newVal);
			ins.setValue(timestamp, newVal);
		}
		
	}
	
	/**
	 * Cette méthode met en forme les données et les écrit dans un fichier en format SPMF
	 * @param outputFile Chemin du fichier de sortie
	 * @throws Exception Liée à la fonction {@link prepareTimestamp}
	 */
	public void toSPMF(String outputFile) throws Exception {
		ArrayList<String> lines = new ArrayList<String>();
		int numAttributes = _data.numAttributes();
		
		// Etiquetage des attributs
		labelize();
		
		// Préparations des horodatages
		//prepareTimestamps();
		
		// Préparation de la chaîne à écrire
		StringBuilder sb = new StringBuilder();
		lines.add("@CONVERTED_FROM_TEXT\n");
		// Attributs et valeurs
		for (Entry<String, ArrayList<Integer>> e : _labels2bins.entrySet()) {
			lines.add("@BIN=" + e.getKey() + "=" + 
					e.getValue().stream().map(Object::toString).collect(Collectors.joining(",")) + "\n");
		}
		// Etiquettes et valeurs
		for (int i = 0; i < _int2labels.size(); ++i) {
			lines.add("@ITEM=" + String.valueOf(i+1) + "=" + _int2labels.get(i) + "\n");
		}
		for (Instance ins : _data) {
			for (int i = 0; i < numAttributes - 2; ++i) {
				sb.append(ins.stringValue(i)).append(" ");
			}
			// NB il faut faire un cast car on renvoie un double, il est donc écrit en format exponentiel
			// ce qui ne sert pas
			sb.append("<" + String.valueOf((int) ins.value(numAttributes - 2)) + "> ");
			// 0 correspond à "pas de panne"
			if (ins.stringValue(numAttributes - 1).equals("0")) {
				sb.append("-1 ");
			} else {
				sb.append("-1 0 -1 -2\n"); // Cette séquence représente la panne
				lines.add(sb.toString());
				sb.setLength(0);
			}
		}
		
		// On enlève la dernière ligne si celle-ci ne correspond pas à une panne
		if (lines.get(lines.size() - 1).endsWith("-1 ")) {
			lines.remove(lines.size() - 1);
		}
		
		// Finalement, on écrit les séquences
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputFile)))) {
			for (String line : lines) {
				bw.write(line);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Exécution du pré-traitement. L'avant-dernière ligne doit correspondre à l'horodatage et la dernière
	 * au test de défaillance.
	 * @param inputFile Fichier d'entrée avec les données pré-formattées
	 * @throws Exception Liée à l'application des divers filtres
	 */
    public void run(String inputFile) throws Exception {
        DataSource source = new DataSource(inputFile);
        _data = source.getDataSet();

        // On transforme la classe (attribut test de panne) en binaire
        NumericToBinary ntb = new NumericToBinary();
        ntb.setAttributeIndices("last");
        ntb.setInputFormat(_data);
        _data = Filter.useFilter(_data, ntb);
        _data.setClassIndex(_data.numAttributes() - 1);

        // On supprime les attributs inutiles (peu ou pas de variance, etc)
        RemoveUseless removeUseless = new RemoveUseless();
        removeUseless.setOptions(new String[] {"-M", "99"});
        removeUseless.setInputFormat(_data);
        _data = Filter.useFilter(_data, removeUseless);

        // On remplit les valeurs manquantes
        ReplaceMissingValues fixMissing = new ReplaceMissingValues();
        fixMissing.setInputFormat(_data);
        _data = Filter.useFilter(_data, fixMissing);

        // On applique l'algorithme de sélection d'attributs
        InfoGainAttributeEval eval = new InfoGainAttributeEval();
        Ranker search = new Ranker();
        search.setOptions(new String[] {"-T", "0.001", "-N", String.valueOf(NATTRIBUTES_TO_KEEP)});
        AttributeSelection attSelect = new AttributeSelection();
        attSelect.setXval(true);
        attSelect.setFolds(10);
        attSelect.setEvaluator(eval);
        attSelect.setSeed(1);
        attSelect.setSearch(search);

        attSelect.SelectAttributes(_data);
        int[] selectedAttributes = attSelect.selectedAttributes();
        int[] attributes = new int[12];
        for (int i = 0; i < NATTRIBUTES_TO_KEEP; ++i) {
            attributes[i] = selectedAttributes[i];
        }
        attributes[10] = _data.numAttributes() - 1;
        attributes[11] = _data.numAttributes();

        // On enlève les attributs non sélectionnés
        Remove remove = new Remove();
        remove.setOptions(new String[] {
            "-R", Arrays.stream(attributes)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(",")),
            "-V"
        });
        remove.setInputFormat(_data);
        _data = Filter.useFilter(_data, remove);
        
        // On discrétise les attributs restants (sauf les deux derniers)
        Discretize discretizeNumeric = new Discretize();
        discretizeNumeric.setOptions(new String[] {
            "-M", "-1.0",
            "-B", "2",
            //"-O",
            "-R", "first-" + (_data.numAttributes() - 2) + ",last",
            "-precision", "6"
        });
        discretizeNumeric.setInputFormat(_data);
        _data = Filter.useFilter(_data, discretizeNumeric);

        _data.sort(_data.numAttributes() - 2); // Tri en timestamp
    }
}
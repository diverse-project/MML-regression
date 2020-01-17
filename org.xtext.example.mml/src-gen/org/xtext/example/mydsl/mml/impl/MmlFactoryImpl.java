/**
 * generated by Xtext 2.20.0
 */
package org.xtext.example.mydsl.mml.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.xtext.example.mydsl.mml.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MmlFactoryImpl extends EFactoryImpl implements MmlFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static MmlFactory init()
  {
    try
    {
      MmlFactory theMmlFactory = (MmlFactory)EPackage.Registry.INSTANCE.getEFactory(MmlPackage.eNS_URI);
      if (theMmlFactory != null)
      {
        return theMmlFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new MmlFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MmlFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case MmlPackage.MML_MODEL: return createMMLModel();
      case MmlPackage.DATA_INPUT: return createDataInput();
      case MmlPackage.CSV_PARSING_CONFIGURATION: return createCSVParsingConfiguration();
      case MmlPackage.ML_CHOICE_ALGORITHM: return createMLChoiceAlgorithm();
      case MmlPackage.ML_ALGORITHM: return createMLAlgorithm();
      case MmlPackage.SVR: return createSVR();
      case MmlPackage.DT: return createDT();
      case MmlPackage.RANDOM_FOREST: return createRandomForest();
      case MmlPackage.SGD: return createSGD();
      case MmlPackage.GTB: return createGTB();
      case MmlPackage.RFORMULA: return createRFormula();
      case MmlPackage.XFORMULA: return createXFormula();
      case MmlPackage.ALL_VARIABLES: return createAllVariables();
      case MmlPackage.PREDICTOR_VARIABLES: return createPredictorVariables();
      case MmlPackage.FORMULA_ITEM: return createFormulaItem();
      case MmlPackage.VALIDATION: return createValidation();
      case MmlPackage.STRATIFICATION_METHOD: return createStratificationMethod();
      case MmlPackage.CROSS_VALIDATION: return createCrossValidation();
      case MmlPackage.TRAINING_TEST: return createTrainingTest();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case MmlPackage.CSV_SEPARATOR:
        return createCSVSeparatorFromString(eDataType, initialValue);
      case MmlPackage.FRAMEWORK_LANG:
        return createFrameworkLangFromString(eDataType, initialValue);
      case MmlPackage.SVM_KERNEL:
        return createSVMKernelFromString(eDataType, initialValue);
      case MmlPackage.VALIDATION_METRIC:
        return createValidationMetricFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case MmlPackage.CSV_SEPARATOR:
        return convertCSVSeparatorToString(eDataType, instanceValue);
      case MmlPackage.FRAMEWORK_LANG:
        return convertFrameworkLangToString(eDataType, instanceValue);
      case MmlPackage.SVM_KERNEL:
        return convertSVMKernelToString(eDataType, instanceValue);
      case MmlPackage.VALIDATION_METRIC:
        return convertValidationMetricToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public MMLModel createMMLModel()
  {
    MMLModelImpl mmlModel = new MMLModelImpl();
    return mmlModel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DataInput createDataInput()
  {
    DataInputImpl dataInput = new DataInputImpl();
    return dataInput;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CSVParsingConfiguration createCSVParsingConfiguration()
  {
    CSVParsingConfigurationImpl csvParsingConfiguration = new CSVParsingConfigurationImpl();
    return csvParsingConfiguration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public MLChoiceAlgorithm createMLChoiceAlgorithm()
  {
    MLChoiceAlgorithmImpl mlChoiceAlgorithm = new MLChoiceAlgorithmImpl();
    return mlChoiceAlgorithm;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public MLAlgorithm createMLAlgorithm()
  {
    MLAlgorithmImpl mlAlgorithm = new MLAlgorithmImpl();
    return mlAlgorithm;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SVR createSVR()
  {
    SVRImpl svr = new SVRImpl();
    return svr;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DT createDT()
  {
    DTImpl dt = new DTImpl();
    return dt;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RandomForest createRandomForest()
  {
    RandomForestImpl randomForest = new RandomForestImpl();
    return randomForest;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SGD createSGD()
  {
    SGDImpl sgd = new SGDImpl();
    return sgd;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public GTB createGTB()
  {
    GTBImpl gtb = new GTBImpl();
    return gtb;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RFormula createRFormula()
  {
    RFormulaImpl rFormula = new RFormulaImpl();
    return rFormula;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public XFormula createXFormula()
  {
    XFormulaImpl xFormula = new XFormulaImpl();
    return xFormula;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public AllVariables createAllVariables()
  {
    AllVariablesImpl allVariables = new AllVariablesImpl();
    return allVariables;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PredictorVariables createPredictorVariables()
  {
    PredictorVariablesImpl predictorVariables = new PredictorVariablesImpl();
    return predictorVariables;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public FormulaItem createFormulaItem()
  {
    FormulaItemImpl formulaItem = new FormulaItemImpl();
    return formulaItem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Validation createValidation()
  {
    ValidationImpl validation = new ValidationImpl();
    return validation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StratificationMethod createStratificationMethod()
  {
    StratificationMethodImpl stratificationMethod = new StratificationMethodImpl();
    return stratificationMethod;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CrossValidation createCrossValidation()
  {
    CrossValidationImpl crossValidation = new CrossValidationImpl();
    return crossValidation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TrainingTest createTrainingTest()
  {
    TrainingTestImpl trainingTest = new TrainingTestImpl();
    return trainingTest;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CSVSeparator createCSVSeparatorFromString(EDataType eDataType, String initialValue)
  {
    CSVSeparator result = CSVSeparator.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertCSVSeparatorToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FrameworkLang createFrameworkLangFromString(EDataType eDataType, String initialValue)
  {
    FrameworkLang result = FrameworkLang.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertFrameworkLangToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SVMKernel createSVMKernelFromString(EDataType eDataType, String initialValue)
  {
    SVMKernel result = SVMKernel.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertSVMKernelToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ValidationMetric createValidationMetricFromString(EDataType eDataType, String initialValue)
  {
    ValidationMetric result = ValidationMetric.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertValidationMetricToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public MmlPackage getMmlPackage()
  {
    return (MmlPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static MmlPackage getPackage()
  {
    return MmlPackage.eINSTANCE;
  }

} //MmlFactoryImpl

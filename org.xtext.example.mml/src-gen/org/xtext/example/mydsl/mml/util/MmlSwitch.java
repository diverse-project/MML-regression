/**
 * generated by Xtext 2.20.0
 */
package org.xtext.example.mydsl.mml.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.xtext.example.mydsl.mml.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.xtext.example.mydsl.mml.MmlPackage
 * @generated
 */
public class MmlSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static MmlPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MmlSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = MmlPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case MmlPackage.MML_MODEL:
      {
        MMLModel mmlModel = (MMLModel)theEObject;
        T result = caseMMLModel(mmlModel);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.DATA_INPUT:
      {
        DataInput dataInput = (DataInput)theEObject;
        T result = caseDataInput(dataInput);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.CSV_PARSING_CONFIGURATION:
      {
        CSVParsingConfiguration csvParsingConfiguration = (CSVParsingConfiguration)theEObject;
        T result = caseCSVParsingConfiguration(csvParsingConfiguration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.ML_CHOICE_ALGORITHM:
      {
        MLChoiceAlgorithm mlChoiceAlgorithm = (MLChoiceAlgorithm)theEObject;
        T result = caseMLChoiceAlgorithm(mlChoiceAlgorithm);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.ML_ALGORITHM:
      {
        MLAlgorithm mlAlgorithm = (MLAlgorithm)theEObject;
        T result = caseMLAlgorithm(mlAlgorithm);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.SVR:
      {
        SVR svr = (SVR)theEObject;
        T result = caseSVR(svr);
        if (result == null) result = caseMLAlgorithm(svr);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.DT:
      {
        DT dt = (DT)theEObject;
        T result = caseDT(dt);
        if (result == null) result = caseMLAlgorithm(dt);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.RANDOM_FOREST:
      {
        RandomForest randomForest = (RandomForest)theEObject;
        T result = caseRandomForest(randomForest);
        if (result == null) result = caseMLAlgorithm(randomForest);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.SGD:
      {
        SGD sgd = (SGD)theEObject;
        T result = caseSGD(sgd);
        if (result == null) result = caseMLAlgorithm(sgd);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.GTB:
      {
        GTB gtb = (GTB)theEObject;
        T result = caseGTB(gtb);
        if (result == null) result = caseMLAlgorithm(gtb);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.RFORMULA:
      {
        RFormula rFormula = (RFormula)theEObject;
        T result = caseRFormula(rFormula);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.XFORMULA:
      {
        XFormula xFormula = (XFormula)theEObject;
        T result = caseXFormula(xFormula);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.ALL_VARIABLES:
      {
        AllVariables allVariables = (AllVariables)theEObject;
        T result = caseAllVariables(allVariables);
        if (result == null) result = caseXFormula(allVariables);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.PREDICTOR_VARIABLES:
      {
        PredictorVariables predictorVariables = (PredictorVariables)theEObject;
        T result = casePredictorVariables(predictorVariables);
        if (result == null) result = caseXFormula(predictorVariables);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.FORMULA_ITEM:
      {
        FormulaItem formulaItem = (FormulaItem)theEObject;
        T result = caseFormulaItem(formulaItem);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.VALIDATION:
      {
        Validation validation = (Validation)theEObject;
        T result = caseValidation(validation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.STRATIFICATION_METHOD:
      {
        StratificationMethod stratificationMethod = (StratificationMethod)theEObject;
        T result = caseStratificationMethod(stratificationMethod);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.CROSS_VALIDATION:
      {
        CrossValidation crossValidation = (CrossValidation)theEObject;
        T result = caseCrossValidation(crossValidation);
        if (result == null) result = caseStratificationMethod(crossValidation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MmlPackage.TRAINING_TEST:
      {
        TrainingTest trainingTest = (TrainingTest)theEObject;
        T result = caseTrainingTest(trainingTest);
        if (result == null) result = caseStratificationMethod(trainingTest);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MML Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MML Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMMLModel(MMLModel object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Data Input</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Data Input</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDataInput(DataInput object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>CSV Parsing Configuration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>CSV Parsing Configuration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCSVParsingConfiguration(CSVParsingConfiguration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ML Choice Algorithm</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ML Choice Algorithm</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMLChoiceAlgorithm(MLChoiceAlgorithm object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ML Algorithm</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ML Algorithm</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMLAlgorithm(MLAlgorithm object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>SVR</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>SVR</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSVR(SVR object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>DT</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>DT</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDT(DT object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Random Forest</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Random Forest</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRandomForest(RandomForest object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>SGD</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>SGD</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSGD(SGD object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>GTB</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>GTB</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGTB(GTB object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>RFormula</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>RFormula</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRFormula(RFormula object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>XFormula</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>XFormula</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseXFormula(XFormula object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>All Variables</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>All Variables</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAllVariables(AllVariables object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Predictor Variables</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Predictor Variables</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePredictorVariables(PredictorVariables object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Formula Item</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Formula Item</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFormulaItem(FormulaItem object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Validation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Validation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseValidation(Validation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Stratification Method</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Stratification Method</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStratificationMethod(StratificationMethod object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Cross Validation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Cross Validation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCrossValidation(CrossValidation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Training Test</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Training Test</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTrainingTest(TrainingTest object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //MmlSwitch


package zhu.minhui.com.shudu.engin;

import android.util.Log;

import java.util.ArrayList;

import zhu.minhui.com.shudu.SoduLevel;

public class MySoduGenerator {
    private static final String TAG = "MySoduGenerator";
    SoduNode[][] soduNodes;
    private char[] charArray;

    public int getNoNeedToSolve() {
        return noNeedSolve;
    }

    public String getDataString() {
        if (charArray == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (char node : charArray) {
            stringBuilder.append(node);
        }
        return stringBuilder.toString();
    }

    private int noNeedSolve;

    // int index = 0;
    //int maxSolutionNum=0;
    public SoduNode[][] generateGame(SoduLevel level) {
        int minSum = level.getMinSum();
        int maxSum = level.getMaxSum();
        charArray = new char[81];
        for (int i = 0; i < 81; i++) {
            charArray[i] = '0';
        }
        soduNodes = SoduUtils.getNodes(charArray);
        noNeedSolve = 0;
        a:
        do {
            int index = getMostSuitableValueIndex();


            int list = index / 9;
            int row = index % 9;
            Log.d(TAG, "select fill node:" + "list:" + list + "row" + row + "index" + index);
            SoduNode currentNode = soduNodes[list][row];
            Integer[] suitValue = currentNode.getSuitValue();
            Integer[] hasSolutionValue = new Integer[9];
            int hasSolutionValueNum = 0;
            if (suitValue.length < 2) {
                continue;
            }
            currentNode.needTobeSolve = false;
            int noTrySuit = suitValue.length;
            boolean hasFound = false;
            while (noTrySuit > 0) {
                int random = (int) (Math.random() * suitValue.length);
                if (suitValue[random] == null) {
                    continue;
                }
                currentNode.value = suitValue[random];
                Log.d(TAG, "suitValue:" + currentNode.value);
                charArray[index] = (char) ('0' + suitValue[random]);

                int solutionNum = getSolutionNum(charArray);
                Log.d(TAG, "solutionNum:" + solutionNum);
                if (solutionNum == 1) {
                    break a;

                } else if (solutionNum > 1) {
                    if (minSum - noNeedSolve > 4) {
                       /* charArray[index] = (char) ('0' + currentNode.value);*/
                        hasFound = true;
                        break;
                    } else {
                        hasSolutionValue[hasSolutionValueNum] = suitValue[random];
                        hasSolutionValueNum++;
                        suitValue[random] = null;
                        noTrySuit--;
                    }

                } else {
                    suitValue[random] = null;
                    noTrySuit--;
                }

            }

            if (!hasFound) {
                Log.d(TAG, "has not found");
                int hasSolutionRandom = (int) (hasSolutionValueNum * Math.random());
                currentNode.value = hasSolutionValue[hasSolutionRandom];
                charArray[index] = (char) ('0' + hasSolutionValue[hasSolutionRandom]);
            }
            noNeedSolve++;
            printsoDuProcess(noNeedSolve);

        } while (true);
        Log.d(TAG, "********************result*********************");
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) {
                Log.d(TAG, "********************************************");
            }
            Log.d(TAG, SoduNode.getNodesValue(soduNodes[i][0].listNode));
        }
        int needEnterNum = (int) (minSum - noNeedSolve + 1 + (maxSum - minSum) * Math.random());
        Log.d(TAG, "need enter num " + needEnterNum);
        if (needEnterNum > 0) {
            enterNum(needEnterNum);
            noNeedSolve = noNeedSolve + needEnterNum;
        }
        return soduNodes;
    }

    private int getMostSuitableValueIndex() {
        int mostSuitable = 1;
        ArrayList<SoduNode> mostSuitValueNodes = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            int y = i / 9;
            int x = i % 9;
            if (!soduNodes[x][y].needTobeSolve) {
                continue;
            }
            Integer[] suitValue = soduNodes[x][y].getSuitValue();
            if (suitValue.length > mostSuitable) {
                mostSuitable = suitValue.length;
                mostSuitValueNodes.clear();
                mostSuitValueNodes.add(soduNodes[x][y]);
            } else if (suitValue.length == mostSuitable) {
                mostSuitValueNodes.add(soduNodes[x][y]);
            }

        }
        int size = mostSuitValueNodes.size();
        SoduNode select = mostSuitValueNodes.get((int) (size * Math.random()));
        return select.yPosition * 9 + select.xPosition;
    }

    private void enterNum(int needEnterNum) {
        if (needEnterNum <= 0) {
            return;
        }
        int enterNum = 0;
        MySolutionFinder mySolutionFinder = new MySolutionFinder();
        SoduNode[][] result = mySolutionFinder.findResult(soduNodes);
        while (true) {
            if (enterNum == needEnterNum) {
                break;
            }
            int x = (int) (9 * Math.random());
            int y = (int) (9 * Math.random());
            if (soduNodes[x][y].value != 0) {
                continue;
            }
            if (soduNodes[x][y].getSuitValue() == null) {
                if (!hasNoSuitTableValue()) {
                    break;
                } else {
                    continue;
                }

            }
            if (soduNodes[x][y].getSuitValue().length < 3) {
                continue;
            }
            soduNodes[x][y].value = result[x][y].value;
            soduNodes[x][y].needTobeSolve = false;
            enterNum++;
        }

    }

    private boolean hasNoSuitTableValue() {
        boolean hasSuitableValue = false;
        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < 9; k++) {
                if (!soduNodes[i][k].needTobeSolve) {
                    continue;
                }
                Integer[] suitValue = soduNodes[i][k].getSuitValue();
                if (suitValue != null && suitValue.length > 2) {
                    return true;
                }
            }
        }
        return false;
    }

    void printsoDuProcess(int time) {
        Log.d(TAG, "********************" + time + "*********************");
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) {
                Log.d(TAG, "********************************************");
            }
            Log.d(TAG, SoduNode.getNodesValue(soduNodes[i][0].listNode));
        }
        Log.d(TAG, "*****************************************");
        Log.d(TAG, "");
        Log.d(TAG, "");
    }

    private int getSolutionNum(char[] charArray) {
        //	maxSolutionNum++;
        return new MySolutionFinder().findSolution(charArray);
    }


    public SoduNode[][] generateEmptyGame() {
        charArray = new char[81];
        for (int i = 0; i < 81; i++) {
            charArray[i] = '0';
        }
        soduNodes = SoduUtils.getNodes(charArray);
        return soduNodes;
    }
}

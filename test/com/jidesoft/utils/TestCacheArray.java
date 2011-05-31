package com.jidesoft.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class TestCacheArray {
    private static boolean compareLists(ArrayList<String> inList1, ArrayList<String> inList2) {
        long before, after;
        boolean matched = true;

        if (inList1.size() != inList2.size()) {
            System.out.println("\tSizes do not much! "
                    + String.valueOf(inList1.size())
                    + " "
                    + String.valueOf(inList2.size()));
            return false;
        }
        before = System.nanoTime();
        for (int i = 0; i < inList1.size(); i++) {
            if (inList1.get(i).compareTo(inList2.get(i)) != 0) {
                System.out.println("\t\tContents at " + i + " do not match! "
                        + String.valueOf(inList1.get(i))
                        + " "
                        + String.valueOf(inList1.get(i)));
                matched = false;
            }
        }
        after = System.nanoTime();
        System.out.println("\tComparison result " + matched + " took "
                + String.valueOf((after - before) / 1000000) + "ms");
        return matched;
    }

    private static boolean compareIndices(ArrayList<String> ControlList, ArrayList<String> CheckList) {
        long before, after;
        boolean matched = true;

        if (ControlList.size() != CheckList.size()) {
            System.out.println("\tSizes do not much! "
                    + String.valueOf(ControlList.size())
                    + " "
                    + String.valueOf(CheckList.size()));
            return false;
        }

        before = System.nanoTime();
        for (int i = 0; i < ControlList.size(); i++) {
            String element = ControlList.get(i);
            if (ControlList.indexOf(element) != CheckList.indexOf(element)) {
                System.out.println("\t\tMismatch for Element " + element
                        + " Control List " + String.valueOf(ControlList.indexOf(element))
                        + " Check List " + String.valueOf(CheckList.indexOf(element)));
                matched = false;
            }
        }
        after = System.nanoTime();
        System.out.println("\tComparison result " + matched + " took "
                + String.valueOf((after - before) / 1000000) + "ms");
        return matched;
    }

    public static void main(String[] args) {

        final String[] playValues1 = {"10", "20", "30", "40", "50", "50", "50", "60", "70", "80", "90", "100"};
        final String[] modifyArray1 = {"20", "50", "70"};
        final String[] modifyArray2 = {"30", "50", "80"};
        final String[] modifyArray3 = {"40", "50", "90"};
        ArrayList<String> myArrayList1 = new ArrayList<String>(Arrays.asList(playValues1));
        CachedArrayList<String> myCachedArrayList1 = new CachedArrayList<String>(Arrays.asList(playValues1));

        // Test remove(int)
        System.out.println("Testing Remove(int)");
        System.out.println("\tComparison Before");
        compareLists(myArrayList1, myCachedArrayList1);
        compareIndices(myArrayList1, myCachedArrayList1);
        myArrayList1.remove(2);
        myCachedArrayList1.remove(2);
        myArrayList1.remove(5);
        myCachedArrayList1.remove(5);
        myArrayList1.remove(myArrayList1.size() - 1);
        System.out.println("\tComparison After");
        myCachedArrayList1.remove(myCachedArrayList1.size() - 1);
        compareLists(myArrayList1, myCachedArrayList1);
        compareIndices(myArrayList1, myCachedArrayList1);

        myArrayList1.clear();
        myCachedArrayList1.clear();
        myCachedArrayList1.invalidateCache(); // need to call explicitly
        myArrayList1.addAll(Arrays.asList(playValues1));
        myCachedArrayList1.addAll(Arrays.asList(playValues1));
        // Test remove (Object)
        System.out.println("Testing Remove(Object)");
        System.out.println("\tComparison Before");
        compareLists(myArrayList1, myCachedArrayList1);
        compareIndices(myArrayList1, myCachedArrayList1);
        myArrayList1.remove("20");
        myCachedArrayList1.remove("20");
        myArrayList1.remove("50");
        myCachedArrayList1.remove("50");
        myArrayList1.remove("90");
        myCachedArrayList1.remove("90");
        myArrayList1.remove("1000");
        myCachedArrayList1.remove("1000");
        System.out.println("\tComparison After");
        compareLists(myArrayList1, myCachedArrayList1);
        compareIndices(myArrayList1, myCachedArrayList1);

        myArrayList1.clear();
        myCachedArrayList1.clear();
        myCachedArrayList1.invalidateCache(); // need to call explicitly
        myArrayList1.addAll(Arrays.asList(playValues1));
        myCachedArrayList1.addAll(Arrays.asList(playValues1));

        // Test add(int, E)
        System.out.println("Testing add(int,E)");
        System.out.println("\tComparison Before");
        compareLists(myArrayList1, myCachedArrayList1);
        compareIndices(myArrayList1, myCachedArrayList1);
        myArrayList1.add(0, "50");
        myCachedArrayList1.add(0, "50");
        myArrayList1.add(7, "50");
        myCachedArrayList1.add(7, "50");
        myArrayList1.add(myArrayList1.size() - 1, "50");
        myCachedArrayList1.add(myCachedArrayList1.size() - 1, "50");
        System.out.println("\tComparison After");
        compareLists(myArrayList1, myCachedArrayList1);
        compareIndices(myArrayList1, myCachedArrayList1);

        myArrayList1.clear();
        myCachedArrayList1.clear();
        myCachedArrayList1.invalidateCache(); // need to call explicitly
        myArrayList1.addAll(Arrays.asList(playValues1));
        myCachedArrayList1.addAll(Arrays.asList(playValues1));

        // Test addAll(int, Collection)
        System.out.println("Testing addAll(int,Collection)");
        System.out.println("\tComparison Before");
        compareLists(myArrayList1, myCachedArrayList1);
        compareIndices(myArrayList1, myCachedArrayList1);
        myArrayList1.addAll(0, Arrays.asList(modifyArray1));
        myCachedArrayList1.addAll(0, Arrays.asList(modifyArray1));
        myArrayList1.addAll(7, Arrays.asList(modifyArray2));
        myCachedArrayList1.addAll(7, Arrays.asList(modifyArray2));
        myArrayList1.addAll(15, Arrays.asList(modifyArray3));
        myCachedArrayList1.addAll(15, Arrays.asList(modifyArray3));
        System.out.println("\tComparison After");
        compareLists(myArrayList1, myCachedArrayList1);
        compareIndices(myArrayList1, myCachedArrayList1);

        myArrayList1.clear();
        myCachedArrayList1.clear();
        myArrayList1.addAll(Arrays.asList(playValues1));
        myCachedArrayList1.addAll(Arrays.asList(playValues1));
        // Test set(int, E)
        System.out.println("Testing set(int,E)");
        System.out.println("\tComparison Before");
        compareLists(myArrayList1, myCachedArrayList1);
        compareIndices(myArrayList1, myCachedArrayList1);
        myArrayList1.set(1, "50");
        myCachedArrayList1.set(1, "50");
        System.out.println("\tComparison After set 1");
        compareLists(myArrayList1, myCachedArrayList1);
        compareIndices(myArrayList1, myCachedArrayList1);
        System.out.println("\tComparison After set 5");
        myArrayList1.set(5, "50");
        myCachedArrayList1.set(5, "50");
        compareLists(myArrayList1, myCachedArrayList1);
        compareIndices(myArrayList1, myCachedArrayList1);
        myArrayList1.set(8, "50");
        myCachedArrayList1.set(8, "50");
        System.out.println("\tComparison After set 8");
        compareLists(myArrayList1, myCachedArrayList1);
        compareIndices(myArrayList1, myCachedArrayList1);

        /*
          int size1 = 50000;
          int size2 = 5000000;
          int skip = 1000;
          final String[] lookFor1 = {"10","1000","10000","40000","20000000"};
          final String[] deleteValues = {"11","1001","10001","40001","20000001"};


          long before, after;

          if (args.length >=1) {
              size1 = Integer.valueOf(args[0]);
              size2 = size1*100;
          }
          if (args.length >=2) {
              skip = Integer.valueOf(args[1]);
          }

          ArrayList<String> myArrayList1 = new ArrayList<String>();
          ArrayList<String> myArrayList2 = new ArrayList<String>();
          initArray(myArrayList1,size1);
          initArray(myArrayList2,size2);
          String[] myArray1 = new String[size1];
          String[] myArray2 = new String[size2];
          String[] myArray3 = new String[size3];
          CachedArrayList<String> myCachedArrayList1 = new CachedArrayList<String>(myArrayList1);
          CachedArrayList<String> myCachedArrayList2 = new CachedArrayList<String>(myArrayList2);
          ActiveCachedArrayList<String> myActiveActiveCachedArrayList1 = new ActiveCachedArrayList<String>(myArrayList1);

          ActiveCachedArrayList<String> myActiveActiveCachedArrayList2 = new ActiveCachedArrayList<String>(myArrayList2);

          ActiveCachedArrayList<String> myLazyActiveCachedArrayList1 = new ActiveCachedArrayList<String>(myArrayList1);
          ActiveCachedArrayList<String> myLazyActiveCachedArrayList2 = new ActiveCachedArrayList<String>(myArrayList2);
          myActiveActiveCachedArrayList2.setLazy(false);
          myActiveActiveCachedArrayList1.setLazy(false);
          myLazyActiveCachedArrayList1.setLazy(true);
          myLazyActiveCachedArrayList2.setLazy(true);

  //		myArrayList1.toArray(myArray1);
  //		myArrayList2.toArray(myArray2);
  //		myArrayList3.toArray(myArray3);
  //		sortArray(myArray1);
  //		sortArray(myArray2);
  //		sortArray(myArray3);

  //		testSpottedIndexOf(myArrayList1,lookFor1);
  //		testSpottedIndexOf(myArrayList2,lookFor1);
  //		testSpottedIndexOf(myArrayList3,lookFor1);
  //		testPartialIndexOf(myArrayList1,skip);
  //		testPartialIndexOf(myArrayList2,skip);
  //		testPartialIndexOf(myArrayList3,skip);

  //		testSpottedIndexOf(myArray1,lookFor1);
  //		testSpottedIndexOf(myArray2,lookFor1);
  //		testSpottedIndexOf(myArray3,lookFor1);
  //		testPartialIndexOf(myArray1,skip);
  //		testPartialIndexOf(myArray2,skip);
  //		testPartialIndexOf(myArray3,skip);
          /*
          compareLists(myCachedArrayList1,myActiveActiveCachedArrayList1);
          compareLists(myCachedArrayList1,myLazyActiveCachedArrayList1);
          compareLists(myCachedArrayList2,myActiveActiveCachedArrayList2);
          compareLists(myCachedArrayList2,myLazyActiveCachedArrayList2);

          System.out.println ("Writing to small Cached Array List");
          testModifications(myCachedArrayList1);
          System.out.println ("Writing to big Cached Array List");
          testModifications(myCachedArrayList2);

          System.out.println ("Writing to small Lazy Cached Array List");
          testModifications(myLazyActiveCachedArrayList1);
          System.out.println ("Writing to big Lazy Cached Array List");
          testModifications(myLazyActiveCachedArrayList2);

          System.out.println ("Writing to small Active Cached Array List");
          testModifications(myActiveActiveCachedArrayList1);
          System.out.println ("Writing to big Active Cached Array List");
          testModifications(myActiveActiveCachedArrayList2);

          compareLists(myCachedArrayList1,myActiveActiveCachedArrayList1);
          compareLists(myCachedArrayList1,myLazyActiveCachedArrayList1);
          compareLists(myCachedArrayList2,myActiveActiveCachedArrayList2);
          compareLists(myCachedArrayList2,myLazyActiveCachedArrayList2);

          testSpottedIndexOf(myCachedArrayList1,lookFor1);
          testSpottedIndexOf(myLazyActiveCachedArrayList1,lookFor1);
          testSpottedIndexOf(myActiveActiveCachedArrayList1,lookFor1);

          testSpottedIndexOf(myCachedArrayList2,lookFor1);
          testSpottedIndexOf(myLazyActiveCachedArrayList2,lookFor1);
          testSpottedIndexOf(myActiveActiveCachedArrayList2,lookFor1);

          testPartialIndexOf(myCachedArrayList1,skip);
          testPartialIndexOf(myLazyActiveCachedArrayList1,skip);
          testPartialIndexOf(myActiveActiveCachedArrayList1,skip);

          System.out.println ("Reading from Cached Array List");
          testPartialIndexOf(myCachedArrayList2,skip);

          System.out.println ("Reading from Lazy Cached Array List");
          testPartialIndexOf(myLazyActiveCachedArrayList2,skip);

          System.out.println ("Reading from Active Cached Array List");
          testPartialIndexOf(myActiveActiveCachedArrayList2,skip);
          */

    }
}

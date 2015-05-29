package com.MyCookBook.Entities;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;

import java.util.ArrayList;

/**
 * Created by shirabd on 09/05/2015.
 */
@ParseClassName("Recipe")
public class Recipe  extends ParseObject {
    public static final String Name = "Name";
    public static final String Category = "Category";
    public static final String SubCategory = "SubCategory";
    public static final String Preparation = "Preparation";
    public static final String Groceries = "Groceries";
    public static final String DishType = "DishType";
    public static final String Difficulty = "Difficulty";
    public static final String KitchenType = "KitchenType";
    public static final String Diet = "Diet";
    public static final String Vegetarian = "Vegetarian";
    public static final String Vegan = "Vegan";
    public static final String RecipePic = "RecipePic.jpg";
    ArrayList<Grocery> groceries = new ArrayList<Grocery>();


    public Recipe() {
    }

    private void putOrDefault(String att,String value){
        if(value==null){
            this.put(att,"");
        }else{
            this.put(att,value);
        }
    }

    private void putBoolean(String att,boolean value){
        this.put(att,String.valueOf(value));
    }

    public void initRecipe(String name,String category,String subCategory,String preparation,String dishType,
                           String difficulty,String kitchenType,boolean diet,boolean vegetarian,boolean vegan){
        setName(name);
        setCategory(category);
        setSubCategory(subCategory);
        setPreparation(preparation);
        //this.putOrDefault(Groceries, groceries);
        setDishType(dishType);
        setDifficulty(difficulty);
        setKitchenType(kitchenType);
        setDiet(diet);
        setVegetarian(vegetarian);
        setVegan(vegan);
        this.saveInBackground();
    }

    public void addRecipe(User user) {
        this.put("createdBy", user);
        this.saveInBackground();
    }

    public void updateGroceries(ArrayList<Grocery> groceries){
        this.put(Groceries, groceries);
        this.saveInBackground();
    }
    public void addGrocery(Grocery grocery){
        groceries.add(grocery);
        this.put(Groceries, groceries);
        this.saveInBackground();
    }

    public void savePic(byte[] data){
        //byte[] data = "Working at Parse is great!".getBytes();
        ParseFile file = new ParseFile(RecipePic, data);
        file.saveInBackground();
        this.put(RecipePic,file);
        this.saveInBackground();
    }

    public String getName(){
        return this.getString(Name);
    }

    public String getCategory(){
        return this.getString(Category);
    }

    public String getSubCategory(){
        return this.getString(SubCategory);
    }

    public String getPreparation(){
        return this.getString(Preparation);
    }

    public String getDishType(){
        return this.getString(DishType);
    }

    public String getDifficulty(){
        return this.getString(Difficulty);
    }

    public String getKitchenType(){
        return this.getString(KitchenType);
    }

    public boolean getDiet(){
        return Boolean.valueOf(this.getString(Diet));
    }

    public boolean getVegetarian(){
        return Boolean.valueOf(this.getString(Vegetarian));
    }
    public boolean getVegan(){
        return Boolean.valueOf(this.getString(Vegan));
    }

    public void setName(String param){
        putOrDefault(Name,param);
    }
    public void setCategory(String param){
        putOrDefault(Category,param);
    }
    public void setSubCategory(String param){
        putOrDefault(SubCategory,param);
    }
    public void setPreparation(String param){
        putOrDefault(Preparation,param);
    }
    public void setDishType(String param){
        putOrDefault(DishType,param);
    }
    public void setDifficulty(String param){
        putOrDefault(Difficulty,param);
    }
    public void setKitchenType(String param){
        putOrDefault(KitchenType,param);
    }
    public void setDiet(boolean param){
        putBoolean(Diet,param);
    }
    public void setVegetarian(boolean param){
        putBoolean(Vegetarian,param);
    }
    public void setVegan(boolean param){
        putBoolean(Vegan,param);
    }
}

package com.example.mycookbook.mycookbook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.MyCookBook.Entities.Album;
import com.MyCookBook.Entities.Grocery;
import com.MyCookBook.Entities.PersonalSettings;
import com.MyCookBook.Entities.Recipe;
import com.MyCookBook.Entities.User;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shirabd on 12/05/2015.
 */
public class Queries {
    static User myUser;
    public static PersonalSettings personalSettings;
//    static HashMap<String,Integer> cacheSettings = new HashMap<String,Integer>();
    static ArrayList<String> sinunCatHeb = new ArrayList<String>();

    static boolean success;
    public static HashMap<String,String> groceriesList = new HashMap<>();
    /*
    This Function get the facebook userId as parameter
    and finds the user object from parse data
    and set it in the "myUser" static variable.
     */
    public static void updateMyUser(String userId) {
        List<ParseObject> userList = null ;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("UserId", userId);

        try {
            userList = query.find();
        } catch (Exception e) {
            myUser = null;
        }
        // Sets the last created user from parse
        myUser = (User) userList.get(userList.size() - 1);
        Log.d("User", "Retrieved " + myUser.getObjectId() + " name=" + myUser.getUserId());
    }

    /*
    The function updates in all the recipies related to the user,
    in the field and value the function gets as parameters
     */
    public static void updateTypeRecipes(String field, String value, User user) {
        ParseQuery<ParseObject> recQuery = ParseQuery.getQuery("Recipe");
        recQuery.whereEqualTo("createdBy", user);
        List<ParseObject> recList = null;
        try {
            recList = recQuery.find();
        } catch (Exception e) {
        }
        for (ParseObject a : recList) {
            a.put(field, value);
            a.saveInBackground();
        }
    }

    /* Returns the all the user's recipes in Recipe type List*/
    public static ArrayList<Recipe> getUserRecipes(User user) {
        ArrayList<Recipe> returnRec = new ArrayList<Recipe>();
        ParseQuery<ParseObject> recQuery = ParseQuery.getQuery("Recipe");
        recQuery.whereEqualTo("createdBy", user);
        List<ParseObject> recList = null;
        try {
            recList = recQuery.find();
        } catch (Exception e) {
            Log.d("Queries Exception", "cannot find recipes for user");
        }
        Log.d("Number of rec:", String.valueOf(recList.size()));

        if (recList != null) {
            for (ParseObject rec : recList) {
                returnRec.add((Recipe) rec);
            }
        }
        return returnRec;
    }

    /*
    When connecting to the facebook this function runs and gets
    the exisiting user from parse, or creating new user when
    it's the first time
     */
    public static void isUserAlreadyExists(String userId) {
        List<ParseObject> userList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("UserId", userId);

        try {
            userList = query.find();
        } catch (Exception e) {
            myUser = null;
            Log.d("score", "Error: " + e.getMessage());
        }
        if (userList!= null && userList.size() != 0) {
            myUser = (User) userList.get(userList.size() - 1);
            Log.d("User", "already exists " + myUser.getObjectId() + " name=" + myUser.getUserId());
        } else {
            myUser = new User();
            myUser.setUserId(userId);
            myUser.saveInBackground();
            Log.d("User", " new User was created " + myUser.getObjectId() + " name=" + myUser.getUserId());
        }
    }

    /*
    The function finds specific Recipe by "Parse" ObjectId
     */
    public static Recipe getRecipeById(String objectId) {
        ParseQuery<ParseObject> recQuery = ParseQuery.getQuery("Recipe");
        recQuery.whereEqualTo("objectId", objectId);
        List<ParseObject> recList = null;
        Recipe retRecipe = null;
        try {
            recList = recQuery.find();
        } catch (Exception e) {
            Log.d("getRecipeById Err", "cannot find Recipe by objId");
        }
        if (recList.size() != 0) {
            retRecipe = (Recipe) recList.get(0);
        }

        return retRecipe;
    }

    public static Album getAlbumById(String objectId) {
        ParseQuery<ParseObject> recQuery = ParseQuery.getQuery("Album");
        recQuery.whereEqualTo("objectId", objectId);
        List<ParseObject> recList = null;
        Album retAlbum = null;
        try {
            recList = recQuery.find();
        } catch (Exception e) {
            Log.d("getRecipeById Err", "cannot find Recipe by objId");
        }
        if (recList.size() != 0) {
            retAlbum = (Album) recList.get(0);
        }

        return retAlbum;
    }

    public static User getUserById(String objectId) {
        ParseQuery<ParseObject> recQuery = ParseQuery.getQuery("User");
        recQuery.whereEqualTo("objectId", objectId);
        List<ParseObject> recList = null;
        User retUser = null;
        try {
            recList = recQuery.find();
        } catch (Exception e) {
            Log.d("getUserById Err", "cannot find Recipe by objId");
        }
        if (recList.size() != 0) {
            retUser = (User) recList.get(0);
        }

        return retUser;
    }


    public static ArrayList<Grocery> getGroceriesById(ArrayList<String> objectIds) {
        ParseQuery<ParseObject> recQuery = ParseQuery.getQuery("Grocery");
        recQuery.whereContainedIn("objectId", objectIds);
        List<ParseObject> recList = null;
        ArrayList<Grocery> gList = new ArrayList<>();
        Recipe retRecipe = null;
        try {
            recList = recQuery.find();
        } catch (Exception e) {
            Log.d("getRecipeById Err", "cannot find Recipe by objId");
        }
        if (recList.size() != 0) {
            for(ParseObject r:recList){
                gList.add((Grocery)r);
            }
        }

        return gList;
    }


    /*Gets the last amount new recipes to present on the feed */
    public static ArrayList<Recipe> getLastRecipes(int amount) {
        ArrayList<Recipe> returnRec = new ArrayList<Recipe>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");

        // Retrieve the most recent ones
        query.orderByDescending("createdAt");

        // Only retrieve the last amount selected
        query.setLimit(amount);

        List<ParseObject> recList = null;
        try {
            recList = query.find();
        } catch (Exception e) {
            Log.d("Queries Exception", "cannot find recipies for user");
        }
        if (recList != null) {
            for (ParseObject rec : recList) {
                returnRec.add((Recipe) rec);
            }
        }
        return returnRec;
    }

    /*
    * params:
    *
    * Diet - should be "true" or "false" or null(will not cosidered in search)
    * vegan - should be "true" or "false" or null(will not cosidered in search)
    * vegetarian - should be "true" or "false" or null(will not cosidered in search)
    *
    * */

    public static ArrayList<Recipe> RecipesSearch(ArrayList<String> category,ArrayList<String> subCategory,ArrayList<String> dishType,String difficulty,String kitchenType, boolean diet,
                                                  boolean vegetarian,boolean vegan, ArrayList<String> groceryIn, ArrayList<String> groceryOut) {
        ArrayList<Recipe> returnRec = new ArrayList<Recipe>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");

        if(subCategory!=null && subCategory.size()!=0) {
            query.whereContainsAll(Recipe.SubCategory, subCategory);
        }

        if(category!=null && category.size()!=0) {
            String []strings = new String[category.size()];
            category.toArray(strings);
            query.whereContainedIn(Recipe.Category, Arrays.asList(strings));
        }

        if(dishType!=null && dishType.size()!=0) {
            String []strings = new String[dishType.size()];
            dishType.toArray(strings);
            query.whereContainedIn(Recipe.DishType, Arrays.asList(strings));
        }

        if(difficulty!=null && difficulty.length()!=0) {
            query.whereEqualTo(Recipe.Difficulty, difficulty);
        }

        if(kitchenType!=null && kitchenType.length()!=0) {
            query.whereEqualTo(Recipe.KitchenType, kitchenType);
        }

        if(diet) {
            query.whereEqualTo(Recipe.Diet, diet);
        }

        if(vegan) {
            query.whereEqualTo(Recipe.Vegan, vegan);
        }

        if(vegetarian) {
            query.whereEqualTo(Recipe.Vegetarian, vegetarian);
        }

        if(groceryIn!=null && groceryIn.size()!=0) {

            //ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("Grocery");
           // innerQuery.whereContainedIn("objectId", groceryIn);
            //innerQuery.whereEqualTo("official", true);
          //  query.whereMatchesQuery(Recipe.Groceries, innerQuery);

            //query.whereContainedIn(Recipe.Groceries, Arrays.asList(getGroceriesById(groceryIn)));
            //query.whereEqualTo(Recipe.Groceries, Arrays.asList(getGroceriesById(groceryIn).get(0)));
            //getGroceriesById()
            query.whereContainedIn(Recipe.Groceries, getGroceriesById(createObjectIdList(groceryIn)));
           // query.include(Recipe.Groceries);

        }

        if(groceryOut!=null && groceryOut.size()!=0){
            query.whereNotContainedIn(Recipe.Groceries, getGroceriesById(createObjectIdList(groceryOut)));
            //query.whereNotContainedIn(Recipe.Groceries, groceryOut);
        }

        //query.orderByDescending("createdAt");

        // Only retrieve the last amount selected
        //query.setLimit(amount);

        List<ParseObject> recList = null;
        try {
            recList = query.find();
        } catch (Exception e) {
            Log.d("Queries Exception", "cannot find recipies for user");
        }
        if (recList != null) {
            for (ParseObject rec : recList) {
                returnRec.add((Recipe) rec);
            }
        }
        return returnRec;
    }



    public static ArrayList<Recipe> RecomendedRecipes(int amount) {
        refreshCachedSettings();
        ArrayList<Recipe> returnRec = new ArrayList<Recipe>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");

        if(sinunCatHeb!=null && sinunCatHeb.size()!=0) {
            String []strings = new String[sinunCatHeb.size()];
            sinunCatHeb.toArray(strings);
            query.whereContainedIn(Recipe.Category, Arrays.asList(strings));
        }

        query.setLimit(amount);
        List<ParseObject> recList = null;
        try {
            recList = query.find();
        } catch (Exception e) {
            Log.d("Queries Exception", "cannot find recipies for user");
        }
        if (recList != null) {
            for (ParseObject rec : recList) {
                returnRec.add((Recipe) rec);
            }
        }
        return returnRec;
    }


    public static ArrayList<Recipe> RecipesSearchPartial(ArrayList<String> category,ArrayList<String> subCategory,ArrayList<String> dishType,String difficulty,String kitchenType, ArrayList<String> groceryIn,
                                                         ArrayList<String> groceryOut) {
        ArrayList<Recipe> returnRec = new ArrayList<Recipe>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");

        if(subCategory!=null && subCategory.size()!=0) {
            query.whereContainsAll(Recipe.SubCategory, subCategory);
        }

        if(category!=null && category.size()!=0) {
            String []strings = new String[category.size()];
            category.toArray(strings);
            query.whereContainedIn(Recipe.Category, Arrays.asList(strings));
        }

        if(dishType!=null && dishType.size()!=0) {
            String []strings = new String[dishType.size()];
            dishType.toArray(strings);
            query.whereContainedIn(Recipe.DishType, Arrays.asList(strings));
        }

        if(difficulty!=null && difficulty.length()!=0) {
            query.whereEqualTo(Recipe.Difficulty, difficulty);
        }

        if(kitchenType!=null && kitchenType.length()!=0) {
            query.whereEqualTo(Recipe.KitchenType, kitchenType);
        }

        if(groceryIn!=null && groceryIn.size()!=0) {

            //ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("Grocery");
            // innerQuery.whereContainedIn("objectId", groceryIn);
            //innerQuery.whereEqualTo("official", true);
            //  query.whereMatchesQuery(Recipe.Groceries, innerQuery);

            //query.whereContainedIn(Recipe.Groceries, Arrays.asList(getGroceriesById(groceryIn)));
            //query.whereEqualTo(Recipe.Groceries, Arrays.asList(getGroceriesById(groceryIn).get(0)));
            //query.whereContainedIn(Recipe.Groceries, groceryIn);
            // query.include(Recipe.Groceries);
            query.whereContainedIn(Recipe.Groceries, getGroceriesById(createObjectIdList(groceryIn)));


        }

        if(groceryOut!=null && groceryOut.size()!=0){
            //query.whereNotContainedIn(Recipe.Groceries, groceryOut);
            query.whereNotContainedIn(Recipe.Groceries, getGroceriesById(createObjectIdList(groceryOut)));

        }

        //query.orderByDescending("createdAt");

        // Only retrieve the last amount selected
        //query.setLimit(amount);

        List<ParseObject> recList = null;
        try {
            recList = query.find();
        } catch (Exception e) {
            Log.d("Queries Exception", "cannot find recipies for user");
        }
        if (recList != null) {
            for (ParseObject rec : recList) {
                returnRec.add((Recipe) rec);
            }
        }
        return returnRec;
    }


    public static User getMyUser() {
        return myUser;
    }

    public static void eraseCurrentUser() {
        myUser = null;
    }

    /*This function return list of albums that the user is related to*/
    public static ArrayList<Album> getUserAlbum(User user) {
        ArrayList<Album> returnAlbum = new ArrayList<Album>();
        ParseQuery<ParseObject> recQuery = ParseQuery.getQuery("Album");

        recQuery.whereEqualTo(Album.Users, user);
        List<ParseObject> recList = null;
        try {
            recList = recQuery.find();
        } catch (Exception e) {
            Log.d("Queries Exception", "cannot find albums for user");
        }
        Log.d("Number of rec:", String.valueOf(recList.size()));
        if (recList != null) {
            for (ParseObject rec : recList) {
                returnAlbum.add((Album) rec);
            }
        }

        return returnAlbum;
    }

    /*This function return list of albums that the user created by himself*/
    public static ArrayList<Album> getAlbumUserCreated(User user) {
        ArrayList<Album> returnAlbum = new ArrayList<Album>();
        ParseQuery<ParseObject> recQuery = ParseQuery.getQuery("Album");

        recQuery.whereEqualTo("CreatedBy", user);
        List<ParseObject> recList = null;
        try {
            recList = recQuery.find();
        } catch (Exception e) {
            Log.d("Queries Exception", "cannot find albums for user");
        }
        Log.d("Number of rec:", String.valueOf(recList.size()));
        if (recList != null) {
            for (ParseObject rec : recList) {
                returnAlbum.add((Album) rec);
            }
        }

        return returnAlbum;
    }

    public static Bitmap getProfilePicture() {
        byte[] data= null;
        Bitmap bmp = null;
        ParseFile applicantResume = (ParseFile) myUser.get("Profile");

        try {
            data = applicantResume.getData();
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        }catch(Exception e){
            Log.e("User Profile Picture:","cannot retrieve picture");
        }

        return bmp;
    }

    public static Bitmap getProfilePictureOfUser(User user) {
        byte[] data= null;
        Bitmap bmp = null;
        ParseFile applicantResume = (ParseFile) user.get("Profile");

        try {
            data = applicantResume.getData();
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        }catch(Exception e){
            Log.e("User Profile Picture:","cannot retrieve picture");
        }

        return bmp;
    }


    public static void refreshAllGroceries(){
        List<ParseObject> returnList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Grocery");

        try {
            returnList = query.find();
        } catch (Exception e) {
        }

        for(ParseObject grc:returnList){
            if(!groceriesList.containsValue(((Grocery) grc).getMaterialName()))
                groceriesList.put(grc.getObjectId(), ((Grocery) grc).getMaterialName());
        }
    }

    public static ArrayList<String> createObjectIdList(ArrayList<String> selectedGroceries){
        ArrayList<String> returnKeys = new ArrayList<String>();

        for (Map.Entry<String, String> e : groceriesList.entrySet()) {
            String key = e.getKey();
            String value = e.getValue();
            if(selectedGroceries.contains(value)){
                returnKeys.add(key);
            }
        }

        return returnKeys;
    }

    public static ArrayList<Recipe> getTopRatedRecipes(int amount){
        ArrayList<Recipe> returnKeys = new ArrayList<Recipe>();
        List<ParseObject> recList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
        query.orderByDescending(Recipe.LikesCounter);
        query.setLimit(amount);

        try{
            recList = query.find();
        }catch(Exception e){
            Log.d("Cannot execute 'getTopRatedRecipes'= ",e.getMessage());
        }

        for(ParseObject rec:recList){
            returnKeys.add((Recipe)rec);
        }
        return returnKeys;
    }

    public static ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<User>();
        List<ParseObject> recList = null ;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");

        try {
            recList = query.find();
        } catch (Exception e) {

        }
        if(recList!=null && recList.size()!=0) {
            for (ParseObject user : recList) {
                userList.add((User) user);
            }
        }
        return userList;
    }
    public static void refreshCachedSettings() {
        List<ParseObject> recList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PersonalSettings");
        query.whereEqualTo(PersonalSettings.User, getMyUser().getObjectId());
        try {
            recList = query.find();
        } catch (Exception e) {
            Log.d("HashSettings Error:",e.getMessage());
        }
        HashMap<String,Integer> cacheSettings = new HashMap<String,Integer>();
        if (recList != null && recList.size() != 0) {
            PersonalSettings userSettings = (PersonalSettings) recList.get(0);
            personalSettings = userSettings;

            cacheSettings.put(userSettings.Sweets, new Integer(userSettings.getValue(userSettings.Sweets)));
            cacheSettings.put(userSettings.Breakfast, new Integer(userSettings.getValue(userSettings.Breakfast)));
            cacheSettings.put(userSettings.salads, new Integer(userSettings.getValue(userSettings.salads)));
            cacheSettings.put(userSettings.additions, new Integer(userSettings.getValue(userSettings.additions)));
            cacheSettings.put(userSettings.Drinks, new Integer(userSettings.getValue(userSettings.Drinks)));
            cacheSettings.put(userSettings.BreadAndBaking, new Integer(userSettings.getValue(userSettings.BreadAndBaking)));
            cacheSettings.put(userSettings.RiceAndPastas, new Integer(userSettings.getValue(userSettings.RiceAndPastas)));
            cacheSettings.put(userSettings.Alcohol, new Integer(userSettings.getValue(userSettings.Alcohol)));
            cacheSettings.put(userSettings.Meat, new Integer(userSettings.getValue(userSettings.Meat)));

            ArrayList<Integer> sortList = new ArrayList<Integer>(cacheSettings.values());
            Collections.sort(sortList);
            ArrayList<String> sinunCat = new ArrayList<String>();
            sinunCat.add(getKeyByValue(cacheSettings,sortList.get(sortList.size()-1)));
            sinunCat.add(getKeyByValue(cacheSettings,sortList.get(sortList.size()-2)));
            sinunCat.add(getKeyByValue(cacheSettings,sortList.get(sortList.size()-3)));
            sinunCat.add(getKeyByValue(cacheSettings,sortList.get(sortList.size()-4)));

            for(int i=0;i<sinunCat.size();i++){
                switch (sinunCat.get(i)){
                    case "Sweets":sinunCatHeb.add("מתוקים");
                                 break;
                    case "Breakfast":sinunCatHeb.add("מרקים");
                        break;
                    case "salads":sinunCatHeb.add("סלטים");
                        break;
                    case "additions":sinunCatHeb.add("תוספות");
                        break;
                    case "Drinks":sinunCatHeb.add("שתיה");
                        break;
                    case "BreadAndBaking":sinunCatHeb.add("לחם ומאפים");
                        break;
                    case "RiceAndPastas":sinunCatHeb.add("אורז ופסטה");
                        break;
                    case "Alcohol":sinunCatHeb.add("משקאות");
                        break;
                    case "Meat":sinunCatHeb.add("בשרים");
                        break;

                }
            }
        }
    }

    public static void setPersonalSettings() {
        List<ParseObject> recList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PersonalSettings");
        query.whereEqualTo(PersonalSettings.User, getMyUser().getObjectId());
        try {
            recList = query.find();
        } catch (Exception e) {
            Log.d("HashSettings Error:",e.getMessage());
        }
        HashMap<String,Integer> cacheSettings = new HashMap<String,Integer>();
        if (recList != null && recList.size() != 0) {
            PersonalSettings userSettings = (PersonalSettings) recList.get(0);
            personalSettings = userSettings;
        }
    }

    public static ArrayList<PersonalSettings> getUsersSettings() {
        List<ParseObject> recList = null;
        ArrayList<PersonalSettings> list = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PersonalSettings");
        //query.whereEqualTo(PersonalSettings.User, getMyUser().getObjectId());
        try {
            recList = query.find();
        } catch (Exception e) {
            Log.d("HashSettings Error:", e.getMessage());
        }
        HashMap<String, Integer> cacheSettings = new HashMap<String, Integer>();
        if (recList != null && recList.size() != 0) {
            PersonalSettings userSettings = null;
            for(ParseObject object: recList){
                userSettings = (PersonalSettings)object;
                if(!String.valueOf(userSettings.getValue(PersonalSettings.Classify)).equals("0")){
                    list.add(userSettings);
                }
            }
        }

        return list;
    }


    public static String getKeyByValue(HashMap<String,Integer> hash, Integer intValue) {
        String strKey = null;
        for (Map.Entry entry : hash.entrySet()) {
            if (intValue.equals(entry.getValue())) {
                strKey = entry.getKey().toString();
                break; //breaking because its one to one map
            }
        }
        return strKey;
    }

    public static String convertCategoryName(String category){
        String returnValue = null;
        switch (category){
            case "מתוקים": returnValue =PersonalSettings.Sweets;
                            break;
            case "מרקים":returnValue =PersonalSettings.soups;
                break;
            case "סלטים":returnValue  = PersonalSettings.salads;
                break;
            case "תוספות":returnValue  = PersonalSettings.additions;
                break;
            case "שתיה":returnValue = PersonalSettings.Drinks;
                break;
            case "לחם ומאפים":returnValue  = PersonalSettings.BreadAndBaking;
                break;
            case "אורז ופסטה":returnValue = PersonalSettings.RiceAndPastas;
                break;
            case "משקאות":returnValue = PersonalSettings.Alcohol;
                break;
            case "בשרים":returnValue = PersonalSettings.Meat;
                break;

        }
        return returnValue;
    }
}

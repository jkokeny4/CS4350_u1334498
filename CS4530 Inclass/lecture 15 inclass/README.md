# Lecture 15 In-Class: Text Summarizer

This Android app uses the Gemini API to create a concise summary of text entered by the user.

## One-time private setup

1. In Google AI Studio, delete the key that was shown in the screenshot and create a new key.
2. In this folder, make a copy of `secrets.properties.example` named `secrets.properties`.
3. Replace `PASTE_YOUR_NEW_KEY_HERE` with the new key, with no quotation marks.
4. Open this folder in Android Studio, wait for Gradle sync to finish, choose an emulator or connected Android phone, and press Run.

`secrets.properties` is ignored by Git so your key will not be uploaded to your repository.

## Evidence to submit

Run the app, enter a paragraph, and tap **Summarize**. Take a screenshot that visibly includes both the original text field and the generated Summary section. The input remains on screen after summarization specifically for this snapshot.

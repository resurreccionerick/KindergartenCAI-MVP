package com.example.myapplication.Teacher.Quiz

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class QuizPresenter(val view: QuizActivity) : QuizContract.presenter {
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseFirestore = FirebaseFirestore.getInstance()
    private var databaseReference = FirebaseDatabase.getInstance().reference
    private var reference = firebaseFirestore


    override fun checkAnswer() {
//
//
//        val answered = true;
//        RadioButton rbSelected = findViewById (radioGroup.getCheckedRadioButtonId());
//
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("ScoreRecords")
//            .child(lessonID_picked).child(firebaseAuth.getCurrentUser().getUid())
//            .child("logs").child(list.get(qCounter).getId());
//
//        reference.get().addOnSuccessListener(new OnSuccessListener < DocumentSnapshot >() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                //create new record folder use to know the average of right and wrong in particular question
//
//
//                chartDatabaseReference.child("QuestionRecordToChart")
//                    .child(lessonID_picked).child(firebaseAuth.getCurrentUser().getUid())
//                    .child("result").setValue("none"); //create result child
//
//                DatabaseReference nameSubjRef = FirebaseDatabase . getInstance ().getReference();
//                nameSubjRef.child("ScoreRecords").child(lessonID_picked)
//                    .child("subject").setValue(subject_Title); //create subject child
//
//                nameSubjRef.child("ScoreRecords").child(lessonID_picked)
//                    .child("name").setValue(lessonTitle); //create subject child
//
//                nameSubjRef.child("ScoreRecords").child(lessonID_picked)
//                    .child("grading").setValue(lessonGrading); //create subject child
//
//
//                if (rbSelected.getText().toString().equals(currentQuestion.getAnswer())) {
//                    score++;
//
//                    final MediaPlayer mediaPlayer =
//                        MediaPlayer.create(QuestionWithPicActivity.this, R.raw.correct_sound);
//                    mediaPlayer.start();
//                    final MediaPlayer clapMediaPlayer =
//                        MediaPlayer.create(QuestionWithPicActivity.this, R.raw.clap_sound);
//                    clapMediaPlayer.start();
//
//                    if (documentSnapshot.getString("isStudent").equalsIgnoreCase("active")) {
//                        databaseReference.child("QuizResult").setValue(
//                            "Q" + String.valueOf(qCounter + 1) + ": " + txtQuestion.getText()
//                                .toString() +
//                                    " , Response: " + rbSelected.getText().toString()
//                                .trim() + " = Correct" + "\n"
//                        );
//
//                        chartDatabaseReference.child("QuestionRecordToChart").child(lessonID_picked)
//                            .child(firebaseAuth.getCurrentUser().getUid())
//                            .child(list.get(qCounter).getId()).setValue("Correct");
//                    }
//
//                    Glide.with(QuestionWithPicActivity.this).load(R.drawable.correct_gif)
//                        .into(img_correctDialog); //for dialog to show if correct
//                    score_correctDialog.setText("Score: " + score);
//                    check_dialog.show(); //show the dialog if correct or wrong
//
//                    final Handler handler = new Handler (Looper.getMainLooper());
//                    handler.postDelayed(new Runnable () {
//                        @Override
//                        public void run() {
//                            check_dialog.dismiss();
//                        }
//                    }, 2000);
//
//
//                } else {
//                    final MediaPlayer mediaPlayer =
//                        MediaPlayer.create(QuestionWithPicActivity.this, R.raw.wrong_sound);
//                    mediaPlayer.start();
//
//                    if (documentSnapshot.getString("isStudent").equalsIgnoreCase("active")) {
//                        databaseReference.child("QuizResult").setValue(
//                            "Q" + String.valueOf(qCounter + 1) + ": " + txtQuestion.getText()
//                                .toString() +
//                                    " , Response: " + rbSelected.getText().toString()
//                                .trim() + " = Wrong" + "\n"
//                        );
//
//                        chartDatabaseReference.child("QuestionRecordToChart").child(lessonID_picked)
//                            .child(firebaseAuth.getCurrentUser().getUid())
//                            .child(list.get(qCounter).getId()).setValue("Wrong");
//                    }
//
//                    Glide.with(QuestionWithPicActivity.this).load(R.drawable.wrong_gif)
//                        .into(img_correctDialog); //for dialog to show if correct
//                    score_correctDialog.setText("Score: " + score);
//                    check_dialog.show(); //show the dialog if correct or wrong
//
//                    final Handler handler = new Handler (Looper.getMainLooper());
//                    handler.postDelayed(new Runnable () {
//                        @Override
//                        public void run() {
//                            check_dialog.dismiss();
//
//                        }
//                    }, 2000);
//                }
    }

    override fun showNextQuestion() {
        TODO("Not yet implemented")
    }
}
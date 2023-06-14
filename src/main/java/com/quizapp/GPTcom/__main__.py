import argparse
import json
import openai
from ast import *

# Set your OpenAI API key here
API_KEY = "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"

# Configure the OpenAI API client
openai.api_key = API_KEY

# Define the quiz generator logic here


def generate_quiz(num_questions, quiz_type, topic, difficulty):
    # Generate the quiz based on the provided parameters
    # You can integrate with OpenAI GPT-3.5 or any other logic to generate the quiz questions and answers

    # Example code using OpenAI GPT-3.5
    response = openai.Completion.create(
        engine="text-davinci-003",
        prompt=f"Generate a quiz with {num_questions} {quiz_type} questions on the topic of {topic} of {difficulty} also generate the id randomly from 1-8 digits long too"+ '''difficulty with Answers,i will store it in a json, here is an example as to how to fromat the generated questions. 
        [
          {
            "id": 2123,
            "type": "MULTIPLE_CHOICE",
            "difficulty": "(difficulty provided in the first half of the prompt)",
            "topic": "History",
            "questions": [
              {
                "question": "What year did World War II end?",
                "options": ["1942", "1945", "1950", "1939"],
                "answer": "1945"
              },
              {
                "question": "Who was the first President of the United States?",
                "options": ["George Washington", "Thomas Jefferson", "Abraham Lincoln", "John Adams"],
                "answer": "George Washington"
              }
            ]
          },
          {
            "id": 132155,
            "type": "TRUE_FALSE",
            "difficulty": "difficulty provided in the first half of the prompt",
            "topic": "Science",
            "questions": [
              {
                "question": "Gravity is a force that pulls objects towards the center of the Earth.",
                "answer": "True"
              },
              {
                "question": "Water boils at 100 degrees Celsius.",
                "answer": "True"
              }
            ]
          }
        ]''',
        max_tokens=2000,
        n=1,
        stop=None,
        temperature=0.8,
        frequency_penalty=0.2,
        presence_penalty=0.0
    )
    print(response)
    # Extract the generated questions from the API response
    quiz_text = response.choices[0].text.strip()

    # Split the quiz into individual questions and answers
    # Store the quiz questions and answers in JSON format
    quiz_json = {"quiz": literal_eval(quiz_text)}

    # Prepare the JSON file name for storing the quiz and answers
    quiz_json_filename = f"src/Res/quiz_{quiz_type}_{difficulty}_{topic}.json"

    # Save the quiz questions and answers to a JSON file
    with open(quiz_json_filename, "w", encoding="utf-8") as quiz_file:
        json.dump(quiz_json, quiz_file, indent=4, ensure_ascii=False)

    return quiz_json


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--num_questions", type=int, help="Number of questions to generate")
    parser.add_argument("--quiz_type", type=str, help="Quiz type")
    parser.add_argument("--topic", type=str, help="Quiz topic")
    parser.add_argument("--difficulty", type=str, help="Quiz difficulty")
    args = parser.parse_args()
    retries = 0

    while retries < 3:
        try:
            generated_quiz = generate_quiz(args.num_questions, args.quiz_type, args.topic, args.difficulty)
        except Exception as e:
            if 'Too many requests' not in str(e): raise e
        retries += 1

    # Print the generated quiz JSON data with proper formatting
    print(json.dumps(generated_quiz, indent=4, ensure_ascii=False))


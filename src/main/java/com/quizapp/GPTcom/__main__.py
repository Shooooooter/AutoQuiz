import argparse
import json
import openai

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
        prompt="Generate a quiz with {} {} questions on the topic of {} with {} difficulty with Answers.".format(
            num_questions, quiz_type, topic, difficulty),
        max_tokens=2000,
        n=1,
        stop=None,
        temperature=0.8,
        frequency_penalty=0.2,
        presence_penalty=0.0
    )

    # Extract the generated questions from the API response
    quiz_text = response.choices[0].text.strip()

    # Split the quiz into individual questions and answers
    quiz_list = quiz_text.split("\n\n")

    # Prepare the quiz questions and answers
    quiz_data = []
    for i, question in enumerate(quiz_list, start=1):
        question = question.strip()
        if question:
            prompt, answer = question.split("\n", 1)
            quiz_data.append({"prompt": f"Question {i}: {prompt}", "answer": answer})

    # Store the quiz questions and answers in JSON format
    quiz_json = {"quiz": quiz_data}

    # Prepare the JSON file name for storing the quiz and answers
    quiz_json_filename = f"src/Res/quiz_{quiz_type}_{difficulty}_{topic}.json"

    # Save the quiz questions and answers to a JSON file
    with open(quiz_json_filename, "w", encoding="utf-8") as quiz_file:
        json.dump(quiz_json, quiz_file, indent=4)

    return quiz_json


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--num_questions", type=int, help="Number of questions to generate")
    parser.add_argument("--quiz_type", type=str, help="Quiz type")
    parser.add_argument("--topic", type=str, help="Quiz topic")
    parser.add_argument("--difficulty", type=str, help="Quiz difficulty")
    args = parser.parse_args()

    generated_quiz = generate_quiz(args.num_questions, args.quiz_type, args.topic, args.difficulty)

    # Print the generated quiz JSON data with proper formatting
    print(json.dumps(generated_quiz, indent=4, ensure_ascii=False))

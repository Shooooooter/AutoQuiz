import argparse
import csv
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
        prompt="Generate a quiz with {} {} questions on the topic of {} with {} difficulty.".format(
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
    quiz_list = quiz_text.split("Answer:")

    # Prepare the CSV file for storing the quiz and answers
    quiz_json_filename = f"quiz_{quiz_type}_{difficulty}_{topic}.json"
    answers_json_filename = f"answers_{quiz_type}_{difficulty}_{topic}.json"

    # Store the quiz questions and answers in JSON format
    quiz_data = []
    answers_data = []
    for question in quiz:
        quiz_data.append({"prompt": question["prompt"], "answer": ""})
        answers_data.append({"prompt": question["prompt"], "answer": question["answer"]})

    with open(quiz_json_filename, "w", encoding="utf-8") as quiz_file:
        json.dump(quiz_data, quiz_file, indent=4)

    with open(answers_json_filename, "w", encoding="utf-8") as answers_file:
        json.dump(answers_data, answers_file, indent=4)

    print(f"Quiz JSON file: {quiz_json_filename}")
    print(f"Answers JSON file: {answers_json_filename}")


def get_correct_answer(question):
    # Use the OpenAI GPT-3.5 model to generate the answer for the provided question
    response = openai.Completion.create(
        engine="text-davinci-003",
        prompt=f"Whats the correct answer of {question}",
        max_tokens=1500,
        n=1,
        stop=None,
        temperature=0.8,
        frequency_penalty=0.0,
        presence_penalty=0.0
    )

    # Extract the generated answer from the API response
    answer = response.choices[0].text.strip()
    return answer


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--num_questions", type=int, help="Number of questions to generate")
    parser.add_argument("--quiz_type", type=str, help="Quiz type")
    parser.add_argument("--topic", type=str, help="Quiz topic")
    parser.add_argument("--difficulty", type=str, help="Quiz difficulty")
    args = parser.parse_args()

    generate_quiz(args.num_questions, args.quiz_type, args.topic, args.difficulty)

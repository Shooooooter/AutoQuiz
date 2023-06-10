import requests


class GPT3Client:
    def __init__(self, api_key):
        self.api_key = api_key
        self.url = "https://api.openai.com/v1/engines/davinci-codex-2/completions"
        self.headers = {
            "Content-Type": "application/json",
            "Authorization": f"Bearer {self.api_key}",
        }

    def generate_text(self, prompt, max_tokens=1024, n=1, stop=None, temperature=0.5):
        data = {
            "prompt": prompt,
            "max_tokens": max_tokens,
            "n": n,
            "stop": stop,
            "temperature": temperature,
        }
        response = requests.post(self.url, headers=self.headers, json=data)
        if response.status_code == 200:
            return response.json()
        else:
            raise ValueError("Request failed with status code:", response.status_code)


class QuizGeneratorService:
    def __init__(self, api_key):
        self.gpt3_client = GPT3Client(api_key)

    def generate_quiz(self, topic, difficulty, num_questions):
        # Define the prompt to generate the quiz questions
        prompt = f"Please generate a {difficulty} difficulty {num_questions}-question quiz on {topic}."

        try:
            # Generate the quiz questions using GPT-3
            response = self.gpt3_client.generate_text(prompt, max_tokens=3024, n=1, temperature=0.7)

            # Parse the response to extract the generated quiz questions
            quiz_questions = response["choices"][0]["text"]

            # Define the prompt to generate the quiz answers
            prompt = f"Please generate the answers for the {num_questions} quiz questions on {topic}."

            # Generate the quiz answers using GPT-3
            response = self.gpt3_client.generate_text(prompt, max_tokens=3024, n=1, temperature=0.7)

            # Parse the response to extract the generated quiz answers
            quiz_answers = response["choices"][0]["text"]

            # Split the quiz questions and answers into separate lists
            quiz_questions_list = quiz_questions.split("\n")
            quiz_answers_list = quiz_answers.split("\n")

            # Remove any empty elements from the lists
            quiz_questions_list = list(filter(None, quiz_questions_list))
            quiz_answers_list = list(filter(None, quiz_answers_list))

            # Validate that the number of questions matches the number of answers
            if len(quiz_questions_list) != len(quiz_answers_list):
                raise ValueError("Number of questions does not match the number of answers.")

            # Create a list of tuples containing the question-answer pairs
            quiz_data = list(zip(quiz_questions_list, quiz_answers_list))

            # Return the generated quiz data
            return quiz_data
        except Exception as e:
            raise ValueError(f"Error generating quiz with prompt '{prompt}': {e}")

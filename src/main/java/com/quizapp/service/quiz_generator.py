import requests
import json

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
        # Define the prompt to generate the quiz
        prompt = f"Please generate a {difficulty} {num_questions}-question quiz on {topic}."

        try:
            # Generate the quiz using GPT-3
            response = self.gpt3_client.generate_text(prompt, max_tokens=1024, n=1, temperature=0.7)

            # Parse the response to extract the generated quiz
            quiz = response["choices"][0]["text"]

            # Return the generated quiz
            return quiz
        except Exception as e:
            raise ValueError(f"Error generating quiz with prompt '{prompt}': {e}")

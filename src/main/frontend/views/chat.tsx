import { Button, TextField } from "@vaadin/react-components";
import { useState } from "react";
import {ChatAIService} from "Frontend/generated/endpoints";
import Markdown from "react-markdown";

export default function Chat() {
    const [message, setMessage] = useState("");
    const [response,setResponse]=useState<string>("")
    const handleSend = () => {
        ChatAIService.ragChat(message).then(resp=>{
            setResponse(resp)
        })
        if (message.trim()) {
            console.log("Message envoyé:", message);
            // Ici, vous pouvez ajouter la logique pour envoyer le message
            setMessage("");
        }
    };

    return (
        <div className="p-m">
            <h3>Chat Bot</h3>
            <div className="flex items-center space-x-2">
                <TextField
                    style={{ flexGrow: 1 }}
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    onKeyPress={(e) => e.key === 'Enter' && handleSend()}
                    placeholder="Tapez votre message..."
                />
                <Button
                    theme="primary"
                    onClick={handleSend}
                    disabled={!message.trim()}
                >
                    Envoyer
                </Button>
                <div>
                    <Markdown>
                        {response}
                    </Markdown>
                </div>

            </div>
        </div>
    );
}
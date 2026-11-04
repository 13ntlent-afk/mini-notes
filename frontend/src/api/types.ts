export interface Note {
  id: number;
  title: string;
  body: string;
  createdAt: string;
  updatedAt: string;
}

export interface ContactMessage {
  id: number;
  userId: number;
  name: string;
  email: string;
  subject: string | null;
  message: string;
  createdAt: string;
}
import { UtilizerDTO } from './utilizerDTO.model';

export interface SqueaksDTO {
  id: number;
  content: string;
  likes: number;
  image: number[];
  imageContentType: string;
  video: number[];
  videoContentType: string;
  utilizer: UtilizerDTO;
}

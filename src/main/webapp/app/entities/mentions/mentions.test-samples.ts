import { IMentions, NewMentions } from './mentions.model';

export const sampleWithRequiredData: IMentions = {
  id: 30572,
  handle: 'dedicated Fork interface',
};

export const sampleWithPartialData: IMentions = {
  id: 60457,
  handle: 'JSON',
};

export const sampleWithFullData: IMentions = {
  id: 9735,
  handle: 'e-markets encoding Designer',
};

export const sampleWithNewData: NewMentions = {
  handle: 'Specialist Licensed Quality-focused',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

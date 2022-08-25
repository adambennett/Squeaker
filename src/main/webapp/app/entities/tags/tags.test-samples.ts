import { ITags, NewTags } from './tags.model';

export const sampleWithRequiredData: ITags = {
  id: 54200,
  hashtag: 'Somalia',
};

export const sampleWithPartialData: ITags = {
  id: 14988,
  hashtag: 'Avon interface',
};

export const sampleWithFullData: ITags = {
  id: 24767,
  hashtag: 'Tasty',
};

export const sampleWithNewData: NewTags = {
  hashtag: 'index Metrics',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
